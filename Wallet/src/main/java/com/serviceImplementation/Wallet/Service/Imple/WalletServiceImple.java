package com.serviceImplementation.Wallet.Service.Imple;

import com.serviceImplementation.Wallet.CustomException.*;
import com.serviceImplementation.Wallet.CustomException.IllegalArgumentException;
import com.serviceImplementation.Wallet.Service.WalletService;
import com.serviceImplementation.Wallet.model.Transactions;
import com.serviceImplementation.Wallet.model.Wallet;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ogs.switchon.common.hibernate_loader.CommonHibernateDAO;
import ogs.switchon.common.hibernate_loader.HibernateSessionFactoryHelper;

import java.util.*;

@Service
public class WalletServiceImple implements WalletService {


    @Value("${fund.transfer.limit}")
    private double transferlimit;

    @Value("${top.Up.limit}")
    private double topuplimit;

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Wallet createWallet(Wallet newWallet) throws UserNotFoundException, UserAlreadyExistException {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Check if mobile number already exists
            Query<Wallet> mobileNumberQuery = session.createQuery("FROM Wallet WHERE mobileNumber = :mobileNumber", Wallet.class);
            mobileNumberQuery.setParameter("mobileNumber", newWallet.getMobileNumber());
            List<Wallet> existingMobileNumber = mobileNumberQuery.getResultList();

            if (!existingMobileNumber.isEmpty()) {
                throw new UserAlreadyExistException("Mobile Number already exists: " + newWallet.getMobileNumber());
            }

            // Check if username already exists
            Query<Wallet> usernameQuery = session.createQuery("FROM Wallet WHERE username = :username", Wallet.class);
            usernameQuery.setParameter("username", newWallet.getUsername());
            List<Wallet> existingUsername = usernameQuery.getResultList();

            if (!existingUsername.isEmpty()) {
                throw new UserAlreadyExistException("Username already exists: " + newWallet.getUsername());
            }
            // Set initial balance and save the wallet
            newWallet.setBalance(0);
            session.save(newWallet);
            transaction.commit();
            return newWallet;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback transaction if exception occurs
            }
            throw new UserNotFoundException("Wallet Creation Unsuccessful.", e);
        } finally {
            if (session != null) {
                session.close(); // Close the session in the finally block
            }
        }
    }

    @Transactional
    public List<Wallet> getAllWallets() throws WalletNotFoundException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            List<Wallet> walletList = CommonHibernateDAO.getAllObjectsSorted(Wallet.class, new String[]{"id"}, "DESC", session);

            if (walletList.isEmpty()) {
                throw new WalletNotFoundException("No wallets found");
            }

            session.getTransaction().commit();
            return walletList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch wallets.", e);
        } finally {
            if (session != null) {
                session.close(); // Close the session in the finally block
            }
        }
    }


    @Transactional
    public Wallet topup(long walletId, Wallet walletRequest) throws IllegalArgumentException, TopUpLimitExceededException, WalletNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Wallet wallet = session.get(Wallet.class, walletId);
            if (wallet == null) {
                throw new WalletNotFoundException("Wallet Not Present " + walletId);
            }

            double topUpAmount = walletRequest.getBalance();

            if (topUpAmount < 0) {
                throw new IllegalArgumentException("Top-up amount cannot be negative");
            }

            if (topUpAmount > topuplimit) {
                throw new TopUpLimitExceededException("Top-up amount exceeds limit");
            }

            wallet.setBalance(wallet.getBalance() + topUpAmount);
            session.update(wallet);
            session.getTransaction().commit();

            return wallet;
        } catch (Exception e) {
            throw new RuntimeException("Top-up Failed.", e);
        }
    }


    @Transactional
    public Double checkBalance(long walletId) throws WalletNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Wallet wallet = session.get(Wallet.class, walletId);
            if (wallet == null) {
                throw new WalletNotFoundException("Wallet not found with ID: " + walletId);
            }

            double balance = wallet.getBalance();
            session.getTransaction().commit();

            return balance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to check balance.", e);
        }
    }

    @Transactional
    public String deleteWalletById(long walletId) throws WalletNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Wallet wallet = session.get(Wallet.class, walletId);
            if (wallet == null) {
                throw new WalletNotFoundException("Wallet Not Found");
            }

            if (wallet.getBalance() == 0) {
                session.delete(wallet);
                session.getTransaction().commit();
                return "Wallet Deleted Successfully!";
            } else {
                return "Balance Is not Zero";
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete wallet.", e);
        }
    }

    @Transactional
    public List<Wallet> fundTransfer(long source, long target, Wallet transferAmount) throws InsufficientBalanceException, WalletNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Wallet sourceWallet = session.get(Wallet.class, source);
            Wallet targetWallet = session.get(Wallet.class, target);

            if (sourceWallet != null && targetWallet != null) {
                double sourceBalance = sourceWallet.getBalance();
                double transferBalance = transferAmount.getBalance();

                if (sourceBalance >= transferBalance && transferBalance <= transferlimit) {
                    saveTransactions(session, sourceWallet, targetWallet, transferBalance);

                    sourceWallet.setBalance(sourceWallet.getBalance() - transferBalance);
                    targetWallet.setBalance(targetWallet.getBalance() + transferBalance);

                    // Save updated wallets
                    session.update(sourceWallet);
                    session.update(targetWallet);

                    transaction.commit();

                    return Arrays.asList(sourceWallet, targetWallet);
                } else {
                    transaction.rollback();
                    throw new InsufficientBalanceException("Insufficient balance or transfer amount exceeds the limit.");
                }
            } else {
                transaction.rollback();
                throw new WalletNotFoundException("Desired Wallet Not Found");
            }
        } catch (HibernateException e) {
            throw new RuntimeException("Failed to transfer funds.", e);
        }
    }
    private void saveTransactions(Session session, Wallet sourceWallet, Wallet targetWallet, double transferBalance) {
        Transactions sourceTransactions = new Transactions();
        sourceTransactions.setWallet(sourceWallet);
        sourceTransactions.setTransactionType("Credited");
        sourceTransactions.setAmount(-transferBalance);
        sourceTransactions.setTimestamp(new Date());
        session.save(sourceTransactions);

        Transactions targetTransactions = new Transactions();
        targetTransactions.setWallet(targetWallet);
        targetTransactions.setTransactionType("Debited");
        targetTransactions.setAmount(transferBalance);
        targetTransactions.setTimestamp(new Date());
        session.save(targetTransactions);
    }

    @Transactional
    public List<Transactions> getAllTransactions() throws TransactionNotFoundException {

            try (Session session = sessionFactory.openSession()) {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Transactions> criteriaQuery = builder.createQuery(Transactions.class);
                Root<Transactions> root = criteriaQuery.from(Transactions.class);
                criteriaQuery.select(root);

                List<Transactions> transactionList = session.createQuery(criteriaQuery).getResultList();

                if (transactionList.isEmpty()) {
                    throw new TransactionNotFoundException("No transactions found");
                }
                return transactionList;
            }
        }
    @Transactional
    public List<Transactions> getTransactionByAmount(double amount) throws TransactionNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Transactions> criteriaQuery = builder.createQuery(Transactions.class);
            Root<Transactions> root = criteriaQuery.from(Transactions.class);
            criteriaQuery.select(root).where(builder.equal(root.get("amount"), amount));

            List<Transactions> transactionsByAmount = session.createQuery(criteriaQuery).getResultList();

            if (transactionsByAmount.isEmpty()) {
                throw new TransactionNotFoundException("No transactions found for amount: " + amount);
            }

            return transactionsByAmount;
        }
    }

}













