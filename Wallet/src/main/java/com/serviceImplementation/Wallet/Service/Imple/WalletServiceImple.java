package com.serviceImplementation.Wallet.Service.Imple;

import com.serviceImplementation.Wallet.Config.ExternalPropertyConfig;
import com.serviceImplementation.Wallet.CustomException.*;
import com.serviceImplementation.Wallet.Service.WalletService;
import com.serviceImplementation.Wallet.model.Transactions;
import com.serviceImplementation.Wallet.model.Wallet;
import ogs.switchon.common.hibernate_loader.CommonHibernateDAO;
import ogs.switchon.common.hibernate_loader.HibernateSessionFactoryHelper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.IllegalArgumentException;
import java.util.*;

@Service
public class WalletServiceImple implements WalletService {

    public WalletServiceImple() {
        System.out.println("5");
    }

    @Value("${fund.transfer.limit}")
    private double transferlimit;

    @Value("${top.Up.limit}")
    private double topuplimit;

    @Autowired
    ExternalPropertyConfig externalPropertyConfig;

    @Override
    public Wallet createWallet(Wallet newWallet) throws UserNotFoundException, UserAlreadyExistException {
        Transaction transaction = null;
        try {
            System.out.println("Inside create wallet Api");
            Session session = HibernateSessionFactoryHelper.getSession();
            System.out.println("Session created");
            transaction = session.beginTransaction();

            Map<String, Object> mapmobile = new HashMap<>();
            mapmobile.put("mobileNumber", newWallet.getMobileNumber());
            List<Wallet> existingMobileNumber = CommonHibernateDAO.getObjectsWithPropertiesAndValues(Wallet.class, mapmobile, session);
            if (!existingMobileNumber.isEmpty()) {
                throw new UserAlreadyExistException("Mobile Number already exists: " + newWallet.getMobileNumber());
            }

            Map<String, Object> mapuser = new HashMap<>();
            mapuser.put("username", newWallet.getUsername());
            List<Wallet> existingUsername = CommonHibernateDAO.getObjectsWithPropertiesAndValues(Wallet.class, mapuser, session);
            if (!existingUsername.isEmpty()) {
                throw new UserAlreadyExistException("Username already exists: " + newWallet.getUsername());
            }

            newWallet.setBalance(0);
            CommonHibernateDAO.insertObject(newWallet, session);
            session.getTransaction().commit();

            return newWallet;
        } catch (UserAlreadyExistException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            throw new UserNotFoundException("Wallet Creation Unsuccessful.", e);
        } finally {
            HibernateSessionFactoryHelper.closeSession();
        }
    }

    @Override
    public List<Wallet> getAllWallets() throws WalletNotFoundException {
        System.out.println("Inside get All Wallets api");
        Session session = HibernateSessionFactoryHelper.getSession();
        try {
            List<Wallet> walletList = CommonHibernateDAO.getAllObjectsSorted(Wallet.class, new String[]{"id"}, "ASC", session);

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

    @Override
    public Wallet topup(long walletId, Wallet walletRequest) throws IllegalArgumentException, TopUpLimitExceededException, WalletNotFoundException {
        System.out.println("Inside top up api");
        try {
            Session session = HibernateSessionFactoryHelper.getSession();
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
            HibernateSessionFactoryHelper.closeSession();
        }
    }

    @Override
    public Double checkBalance(long walletId) throws WalletNotFoundException {
        try (Session session = HibernateSessionFactoryHelper.getSession()) {
            session.beginTransaction();

            Wallet wallet = CommonHibernateDAO.getObjectWithId(Wallet.class, walletId, session);
            if (wallet == null) {
                throw new WalletNotFoundException("Wallet not found with ID: " + walletId);
            }

            double balance = wallet.getBalance();
            session.getTransaction().commit();

            return balance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to check balance.", e);
        } finally {
            HibernateSessionFactoryHelper.closeSession();
        }
    }

    @Override
    public String deleteWalletById(long walletId) throws WalletNotFoundException {
        try (Session session = HibernateSessionFactoryHelper.getSession()) {
            session.beginTransaction();

            Wallet wallet = CommonHibernateDAO.getObjectWithId(Wallet.class, walletId, session);
            if (wallet == null) {
                throw new WalletNotFoundException("Wallet not found with ID: " + walletId);
            }

            double balance = wallet.getBalance();
            if (balance > 0) {
                throw new WalletNotFoundException("Cannot delete wallet with ID: " + walletId + " with balance greater than zero.");
            }

            CommonHibernateDAO.deleteObjectWithId(Wallet.class, walletId, session);
            session.getTransaction().commit();

            return "Wallet Deleted Successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete wallet with ID: " + walletId, e);
        } finally {
            HibernateSessionFactoryHelper.closeSession();
        }
    }


    @Override
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
            HibernateSessionFactoryHelper.closeSession();
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

    @Override
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
            HibernateSessionFactoryHelper.closeSession();
        }
    }

    @Override
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
            HibernateSessionFactoryHelper.closeSession();
        }
    }
}

//
//SELECT TOP (1000) [WalletId]
//        ,[Username]
//        ,[MobileNumber]
//        ,[Balance]
//FROM [pt-switchon-switch].[dbo].[wallet_config]
//










