package com.oliver.accountBackend.manager;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.accountBackend.domain.Transaction;
import com.oliver.accountBackend.mapper.AccountMapper;
import com.oliver.accountBackend.mapper.TransactionMapper;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.faker.TransactionFaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@SpringBootTest(classes = Synpulse8BackendApplication.class)
@ActiveProfiles("test")
public class AccountTransactionManagerTest {
    @Autowired
    private AccountManager accountManager;

    @Autowired
    private AccountTransactionManager accountTransactionManager;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private AccountMapper accountMapper;

    @AfterEach
    public void tearDown() {
        accountMapper.removeAllAccountsFromDB();
    }

    @Test
    public void createTransactionTest() throws ValidationException, ConflictException, InterruptedException {
        Transaction fakeTransaction = TransactionFaker.createValidTransaction();

        accountManager
                .createAccount(
                        "Canada",
                        fakeTransaction.getAccountIban()
                );

        Transaction transaction = accountTransactionManager.createTransaction(
                fakeTransaction.getTransactionId(),
                300,
                fakeTransaction.getAccountIban(),
                fakeTransaction.getValueDate(),
                fakeTransaction.getDescription()
        );

        Assertions.assertEquals(fakeTransaction, transaction);

        String tableNameSuffix =
                accountTransactionManager
                        .getTransactionTableNameSuffix(
                                transaction.getAccountIban()
                        );

        Thread.sleep(2000);

        Transaction savedTransaction =
                transactionMapper
                        .getTransactionByTransactionId(
                                transaction.getTransactionId(),
                                tableNameSuffix
                        );

        Assertions.assertEquals(savedTransaction, transaction);

        transactionMapper.dropTransactionTable(tableNameSuffix);
    }

    @Test
    public void createTransactionTestWithNonExistingAccount() {
        Transaction fakeTransaction = TransactionFaker.createValidTransaction();

        Assertions.assertThrows(ValidationException.class, () -> {
            accountTransactionManager.createTransaction(
                    fakeTransaction.getTransactionId(),
                    300,
                    fakeTransaction.getAccountIban(),
                    fakeTransaction.getValueDate(),
                    fakeTransaction.getDescription()
            );
        });
    }

    @Test
    public void createTransactionWithoutTransactionIdAndValueDate() throws ValidationException, ConflictException, InterruptedException {
        Transaction fakeTransaction = TransactionFaker.createValidTransaction();

        accountManager
                .createAccount(
                        "Canada",
                        fakeTransaction.getAccountIban()
                );

        Transaction transaction = accountTransactionManager.createTransaction(
                null,
                500,
                fakeTransaction.getAccountIban(),
                null,
                fakeTransaction.getDescription()
        );

        String tableNameSuffix =
                accountTransactionManager
                        .getTransactionTableNameSuffix(
                                transaction.getAccountIban()
                        );

        Thread.sleep(2000);

        Transaction savedTransaction =
                transactionMapper
                        .getTransactionByTransactionId(
                                transaction.getTransactionId(),
                                tableNameSuffix
                        );

        Assertions.assertEquals(savedTransaction, transaction);

        transactionMapper.dropTransactionTable(tableNameSuffix);
    }

    @Test
    public void getTransactionsByAccountIbanAndValueDate() throws ValidationException, ConflictException {
        Transaction fakeTransaction = TransactionFaker.createValidTransaction();

        accountManager
                .createAccount(
                        "Canada",
                        fakeTransaction.getAccountIban()
                );

        accountManager
                .createAccount(
                        "Canada",
                        "valid-account-iban"
                );

        String tableNameSuffix =
                accountTransactionManager
                        .getTransactionTableNameSuffix(
                                fakeTransaction.getAccountIban()
                        );

        String validIBANTableNameSuffix =
                accountTransactionManager
                        .getTransactionTableNameSuffix(
                                "valid-account-iban"
                        );


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        Transaction transaction1 = new Transaction(
                UUID.randomUUID().toString(),
                "valid-amount1",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description"
        );

        Transaction transaction2 = new Transaction(
                UUID.randomUUID().toString(),
                "valid-amount2",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description"
        );

        Transaction transaction3 = new Transaction(
                UUID.randomUUID().toString(),
                "valid-amount3",
                "valid-account-iban",
                calendar.getTime(),
                "description"
        );

        calendar.add(Calendar.DATE, 1);
        Transaction transaction4 = new Transaction(
                UUID.randomUUID().toString(),
                "valid-amount4",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description"
        );

        Transaction transaction5 = new Transaction(
                UUID.randomUUID().toString(),
                "valid-amount5",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description"
        );

        Date endDate = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Transaction transaction6 = new Transaction(
                UUID.randomUUID().toString(),
                "valid-amount6",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description"
        );

        transactionMapper.createTransactionTable(tableNameSuffix);
        transactionMapper.createTransactionTable(validIBANTableNameSuffix);
        transactionMapper.saveTransaction(transaction1, tableNameSuffix);
        transactionMapper.saveTransaction(transaction2, tableNameSuffix);
        transactionMapper.saveTransaction(transaction3, validIBANTableNameSuffix);
        transactionMapper.saveTransaction(transaction4, tableNameSuffix);
        transactionMapper.saveTransaction(transaction5, tableNameSuffix);
        transactionMapper.saveTransaction(transaction6, tableNameSuffix);

        List<Transaction> transactions =
                accountTransactionManager.getTransactionsByAccountIbanAndValueDate(
                        fakeTransaction.getAccountIban(),
                        startDate,
                        endDate,
                        1,
                        10
                );

        transactions.forEach(System.out::println);
        System.out.println(startDate);
        System.out.println(endDate);

        Assertions.assertEquals(4, transactions.size());
        Assertions.assertTrue(transactions.contains(transaction1));
        Assertions.assertTrue(transactions.contains(transaction2));
        Assertions.assertTrue(transactions.contains(transaction4));
        Assertions.assertTrue(transactions.contains(transaction5));

        transactions =
                accountTransactionManager.getTransactionsByAccountIbanAndValueDate(
                        fakeTransaction.getAccountIban(),
                        startDate,
                        endDate,
                        1,
                        3
                );

        Assertions.assertEquals(3, transactions.size());

        transactions =
                accountTransactionManager.getTransactionsByAccountIbanAndValueDate(
                        fakeTransaction.getAccountIban(),
                        startDate,
                        endDate,
                        2,
                        10
                );

        Assertions.assertEquals(0, transactions.size());

        transactions =
                accountTransactionManager.getTransactionsByAccountIbanAndValueDate(
                        fakeTransaction.getAccountIban(),
                        startDate,
                        endDate,
                        -1,
                        10
                );

        Assertions.assertEquals(4, transactions.size());


        transactionMapper.dropTransactionTable(tableNameSuffix);
        transactionMapper.dropTransactionTable(validIBANTableNameSuffix);
    }
}
