package com.serviceImplementation.Wallet.Service.Imple;

import com.serviceImplementation.Wallet.Config.ExternalPropertyConfig;
import com.serviceImplementation.Wallet.CustomException.*;
import com.serviceImplementation.Wallet.Service.WalletService;
import com.serviceImplementation.Wallet.model.Transactions;
import com.serviceImplementation.Wallet.model.Wallet;
import jakarta.transaction.Transactional;
import ogs.switchon.common.hibernate_loader.CommonHibernateDAO;
import ogs.switchon.common.hibernate_loader.HibernateSessionFactoryHelper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class WalletServiceImple implements WalletService {

    public WalletServiceImple() {
        System.out.println("Wallet Service Constructor is working....");
    }

    @Autowired
    private ExternalPropertyConfig externalPropertyConfig;


    @Value("${fund.transfer.limit}")
    private double transferlimit;
    @Value("${top.Up.limit}")
    private double topuplimit;

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Wallet createWallet(Wallet newWallet) throws UserNotFoundException, UserAlreadyExistException {
        Session session = null;
        try {
            session = HibernateSessionFactoryHelper.getSession();

            // Check if mobile number already exists
            List<Wallet> existingMobileNumber = CommonHibernateDAO.getObjectsWithPropertyAndValue(Wallet.class, "mobileNumber", newWallet.getMobileNumber(), session);
            if (!existingMobileNumber.isEmpty()) {
                throw new UserAlreadyExistException("Mobile Number already exists: " + newWallet.getMobileNumber());
            }

            // Check if username already exists
            List<Wallet> existingUsername = CommonHibernateDAO.getObjectsWithPropertyAndValue(Wallet.class, "username", newWallet.getUsername(), session);
            if (!existingUsername.isEmpty()) {
                throw new UserAlreadyExistException("Username already exists: " + newWallet.getUsername());
            }

            // Set initial balance and save the wallet
            newWallet.setBalance(0);
            CommonHibernateDAO.insertObject(newWallet, session);

            return newWallet;
        } catch (Exception e) {
            throw new UserNotFoundException("Wallet Creation Unsuccessful.", e);
        } finally {
            HibernateSessionFactoryHelper.closeSession();
        }
    }



    @Transactional
    public List<Wallet> getAllWallets() throws WalletNotFoundException {
        Session session = HibernateSessionFactoryHelper.getSession();
        try {
            List<Wallet> walletList = CommonHibernateDAO.getAllObjectsSorted(Wallet.class, new String[]{"id"}, "DESC", session);

            if (walletList.isEmpty()) {
                throw new WalletNotFoundException("No wallets found");
            }

            return walletList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch wallets.", e);
        } finally {
            HibernateSessionFactoryHelper.closeSession();
        }
    }

    @Transactional
    public Wallet topup(long walletId, Wallet walletRequest) throws IllegalArgumentException, TopUpLimitExceededException, WalletNotFoundException {
        try (Session session = HibernateSessionFactoryHelper.getSession()) {
            session.beginTransaction();

            Wallet wallet = CommonHibernateDAO.getObjectWithId(Wallet.class, walletId, session);
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
            CommonHibernateDAO.updateObject(wallet, session);
            session.getTransaction().commit();

            return wallet;
        } catch (Exception e) {
            throw new RuntimeException("Top-up Failed.", e);
        } finally {
            HibernateSessionFactoryHelper.closeSession(); // Close the session in the finally block
        }
    }

    @Transactional
    public Double checkBalance(long walletId) throws WalletNotFoundException {
        try (Session session = HibernateSessionFactoryHelper.getSession()) {
            session.beginTransaction();

            Wallet wallet = CommonHibernateDAO.getObjectWithId(Wallet.class, walletId, session); // Use getObjectWithId method
            if (wallet == null) {
                throw new WalletNotFoundException("Wallet not found with ID: " + walletId);
            }

            double balance = wallet.getBalance();
            session.getTransaction().commit();

            return balance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to check balance.", e);
        } finally {
            HibernateSessionFactoryHelper.closeSession(); // Close the session in the finally block
        }
    }


    @Transactional
    public String deleteWalletById(long walletId) throws WalletNotFoundException {
        try (Session session = HibernateSessionFactoryHelper.getSession()) {
            session.beginTransaction();

            CommonHibernateDAO.deleteObjectWithId(Wallet.class, walletId, session); // Use deleteObjectWithId method
            session.getTransaction().commit();
            return "Wallet Deleted Successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete wallet.", e);
        } finally {
            HibernateSessionFactoryHelper.closeSession(); // Close the session in the finally block
        }
    }


    @Transactional
    public List<Wallet> fundTransfer(long source, long target, Wallet transferAmount) throws InsufficientBalanceException, WalletNotFoundException {
        try (Session session = HibernateSessionFactoryHelper.getSession()) {
            Transaction transaction = session.beginTransaction();

            Wallet sourceWallet = CommonHibernateDAO.getObjectWithId(Wallet.class, source, session);
            Wallet targetWallet = CommonHibernateDAO.getObjectWithId(Wallet.class, target, session);


            if (sourceWallet != null && targetWallet != null) {
                double sourceBalance = sourceWallet.getBalance();
                double transferBalance = transferAmount.getBalance();

                if (sourceBalance >= transferBalance && transferBalance <= transferlimit) {
                    saveTransactions(session, sourceWallet, targetWallet, transferBalance);

                    sourceWallet.setBalance(sourceWallet.getBalance() - transferBalance);
                    targetWallet.setBalance(targetWallet.getBalance() + transferBalance);

                    // Save updated wallets using CommonHibernateDAO
                    CommonHibernateDAO.updateObject(sourceWallet, session);
                    CommonHibernateDAO.updateObject(targetWallet, session);

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
        } finally {
            HibernateSessionFactoryHelper.closeSession(); // Close the session in the finally block
        }
    }


    private void saveTransactions(Session session, Wallet sourceWallet, Wallet targetWallet, double transferBalance) {
        Transactions sourceTransactions = new Transactions();
        sourceTransactions.setWallet(sourceWallet);
        sourceTransactions.setTransactionType("Credited");
        sourceTransactions.setAmount(-transferBalance);
        sourceTransactions.setTimestamp(new Date());
        CommonHibernateDAO.insertObject(sourceTransactions, session);

        Transactions targetTransactions = new Transactions();
        targetTransactions.setWallet(targetWallet);
        targetTransactions.setTransactionType("Debited");
        targetTransactions.setAmount(transferBalance);
        targetTransactions.setTimestamp(new Date());
        CommonHibernateDAO.insertObject(targetTransactions, session);
    }


    @Transactional
    public List<Transactions> getAllTransactions() throws TransactionNotFoundException {
        Session session = HibernateSessionFactoryHelper.getSession();
        try {
            List<Transactions> transactionList = CommonHibernateDAO.getAllObjects(Transactions.class, session);

            if (transactionList.isEmpty()) {
                throw new WalletNotFoundException("No transactions found");
            }

            return transactionList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch transactions.", e);
        } finally {
            HibernateSessionFactoryHelper.closeSession(); // Close the session in the finally block
        }
    }

    @Transactional
    public List<Transactions> getTransactionByAmount(double amount) throws TransactionNotFoundException {
        Session session = HibernateSessionFactoryHelper.getSession();
        try {
            List<Transactions> transactionsByAmount = CommonHibernateDAO.getAllObjects(Transactions.class, session);

            if (transactionsByAmount.isEmpty()) {
                throw new TransactionNotFoundException("No transactions found for amount: " + amount);
            }

            return transactionsByAmount;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch transactions.", e);
        } finally {
            HibernateSessionFactoryHelper.closeSession(); // Close the session in the finally block
        }
    }


}













