package com.oliver.accountBackend.service;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.accountBackend.domain.Transaction;
import com.oliver.accountBackend.manager.AccountManager;
import com.oliver.accountBackend.manager.AccountTransactionManager;
import com.oliver.accountBackend.mapper.TransactionMapper;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.faker.TransactionFaker;
import com.oliver.pagenation.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@SpringBootTest(classes = Synpulse8BackendApplication.class)
@ActiveProfiles("test")
public class AccountTransactionServiceTest {
    @Autowired
    private AccountManager accountManager;

    @Autowired
    private AccountTransactionServiceImpl accountTransactionService;

    @Autowired
    private AccountTransactionManager accountTransactionManager;

    @Autowired
    private TransactionMapper transactionMapper;

    @Test
    public void createTransactionTest() throws ValidationException, ConflictException, InterruptedException {
        Transaction fakeTransaction = TransactionFaker.createValidTransaction();

        accountManager
                .createAccount(
                        "Canada",
                        fakeTransaction.getAccountIban()
                );

        Transaction transaction = accountTransactionService.createTransaction(
                300,
                fakeTransaction.getAccountIban(),
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
    public void createTransactionTestWithNonExistingAccountIban() {
        Transaction fakeTransaction = TransactionFaker.createValidTransaction();

        Assertions.assertThrows(ValidationException.class, () ->
                accountTransactionService.createTransaction(
                        300,
                        fakeTransaction.getAccountIban(),
                        fakeTransaction.getDescription()
                )
        );
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


        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        Transaction transaction1 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD 100",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description"
        );

        calendar.add(Calendar.DATE, 1);
        Transaction transaction2 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD 200",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description"
        );

        Transaction transaction3 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD -200",
                "valid-account-iban",
                calendar.getTime(),
                "description"
        );

        calendar.add(Calendar.DATE, 1);
        Transaction transaction4 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD -50",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description"
        );

        Transaction transaction5 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD -100",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description"
        );

        calendar.add(Calendar.DATE, 1);
        Transaction transaction6 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD 300",
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

        calendar.add(Calendar.DATE, -1);
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DATE, -2);
        Date startDate = calendar.getTime();

        Page<Transaction> transactionPage =
                accountTransactionService.getTransactionsByAccountIbanAndValueDate(
                        fakeTransaction.getAccountIban(),
                        startDate,
                        endDate,
                        1,
                        2
                );

        Assertions.assertEquals(2, transactionPage.getTotal());

        transactionPage =
                accountTransactionService.getTransactionsByAccountIbanAndValueDate(
                        fakeTransaction.getAccountIban(),
                        startDate,
                        endDate,
                        1,
                        3
                );

        Assertions.assertEquals(3, transactionPage.getTotal());

        transactionPage =
                accountTransactionService.getTransactionsByAccountIbanAndValueDate(
                        fakeTransaction.getAccountIban(),
                        startDate,
                        endDate,
                        -1,
                        3
                );

        Assertions.assertEquals(3, transactionPage.getTotal());

        transactionMapper.dropTransactionTable(tableNameSuffix);
        transactionMapper.dropTransactionTable(validIBANTableNameSuffix);
    }
}
