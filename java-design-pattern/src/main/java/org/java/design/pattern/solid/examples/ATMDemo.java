package org.java.design.pattern.solid.examples;


import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

// Main Demo
public class ATMDemo {


    public static void main(String[] args) {
        // Setup
        Map<String, String> users = new HashMap<>();
        users.put("john", "1234");
        users.put("jane", "5678");

        BankAccount account = new BankAccount("john");
        TransactionLogger logger = new ConsoleTransactionLogger();

        AuthenticationService authService = new SimpleAuthService(users);
        WithdrawalService withdrawalService = new SimpleWithdrawalService(account, logger);
        BalanceService balanceService = new SimpleBalanceService(account);

        ATMMachine atm = new ATMMachine(authService, withdrawalService, balanceService, logger);

        // Demo
        atm.login("john", "1234");
        atm.checkBalance();
        atm.withdraw(500);
        atm.checkBalance();
        atm.withdraw(10000);
        atm.logout();
    }
}



// Single Responsibility Principle (SRP)
interface AuthenticationService {
    boolean authenticate(String pin);
}

interface WithdrawalService {
    boolean withdraw(double amount);
}

interface BalanceService {
    double getBalance();
}

interface TransactionLogger {
    void log(String message);
}

// Open/Closed Principle (OCP) & Dependency Inversion Principle (DIP)
@RequiredArgsConstructor
class SimpleAuthService implements AuthenticationService {
    private final Map<String, String> users;
    private String currentUser;

    public void setCurrentUser(String user) {
        this.currentUser = user;
    }

    @Override
    public boolean authenticate(String pin) {
        return users.containsKey(currentUser) && users.get(currentUser).equals(pin);
    }
}

@RequiredArgsConstructor
class BankAccount {
    private final String accountHolder;
    private double balance = 5000.0;

    public double getBalance() {
        return balance;
    }

    public boolean deductBalance(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

@RequiredArgsConstructor
class SimpleWithdrawalService implements WithdrawalService {
    private final BankAccount account;
    private final TransactionLogger logger;

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            logger.log("Invalid withdrawal amount: " + amount);
            return false;
        }

        if (account.deductBalance(amount)) {
            logger.log("Withdrawal successful: $" + amount);
            return true;
        }

        logger.log("Insufficient balance for withdrawal: $" + amount);
        return false;
    }
}

@RequiredArgsConstructor
class SimpleBalanceService implements BalanceService {
    private final BankAccount account;

    @Override
    public double getBalance() {
        return account.getBalance();
    }
}

@RequiredArgsConstructor
class ConsoleTransactionLogger implements TransactionLogger {
    @Override
    public void log(String message) {
        System.out.println("[TRANSACTION LOG] " + message);
    }
}

// Interface Segregation Principle (ISP)
interface ATMOperations {
    void login(String user, String pin);
    void logout();
    void checkBalance();
    void withdraw(double amount);
}

@RequiredArgsConstructor
class ATMMachine implements ATMOperations {
    private final AuthenticationService authService;
    private final WithdrawalService withdrawalService;
    private final BalanceService balanceService;
    private final TransactionLogger logger;

    private boolean isLoggedIn = false;

    @Override
    public void login(String user, String pin) {
        ((SimpleAuthService) authService).setCurrentUser(user);

        if (authService.authenticate(pin)) {
            isLoggedIn = true;
            logger.log("User " + user + " logged in successfully");
        } else {
            logger.log("Invalid credentials for user " + user);
        }
    }

    @Override
    public void logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
            logger.log("User logged out");
        }
    }

    @Override
    public void checkBalance() {
        if (isLoggedIn) {
            double balance = balanceService.getBalance();
            System.out.println("Current Balance: $" + balance);
            logger.log("Balance checked: $" + balance);
        } else {
            logger.log("Access denied: User not logged in");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (isLoggedIn) {
            withdrawalService.withdraw(amount);
        } else {
            logger.log("Access denied: User not logged in");
        }
    }
}


