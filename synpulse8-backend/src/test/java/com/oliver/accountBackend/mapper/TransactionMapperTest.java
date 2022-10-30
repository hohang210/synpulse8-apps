package com.oliver.accountBackend.mapper;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.accountBackend.domain.Transaction;
import com.oliver.faker.TransactionFaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@SpringBootTest(classes = Synpulse8BackendApplication.class)
@ActiveProfiles("test")
public class TransactionMapperTest {
    @Autowired
    private TransactionMapper transactionMapper;

    private final String TABLE_NAME_SUFFIX = "30";

    @BeforeEach
    public void setUp() {
        transactionMapper.createTransactionTable(TABLE_NAME_SUFFIX);
    }

    @AfterEach
    public void tearDown() {
        transactionMapper.dropTransactionTable(TABLE_NAME_SUFFIX);
    }

    @Test
    public void createTransactionTable() {
        Transaction transaction =
                TransactionFaker.createValidTransaction();
        boolean isSaved =
                transactionMapper.saveTransaction(transaction, TABLE_NAME_SUFFIX);

        Assertions.assertTrue(isSaved);

        Transaction savedTransaction =
                transactionMapper.getTransactionByTransactionId(
                        transaction.getTransactionId(),
                        TABLE_NAME_SUFFIX
                );

        Assertions.assertEquals(savedTransaction, transaction);

        Transaction transactionWithEmptyDescription =
                TransactionFaker.createEmptyDescriptionTransaction();
        isSaved = transactionMapper.saveTransaction(
                transactionWithEmptyDescription,
                TABLE_NAME_SUFFIX
        );

        Assertions.assertTrue(isSaved);

        savedTransaction =
                transactionMapper.getTransactionByTransactionId(
                        transactionWithEmptyDescription.getTransactionId(),
                        TABLE_NAME_SUFFIX
                );

        Assertions.assertEquals(savedTransaction, transactionWithEmptyDescription);
    }

    @Test
    public void saveDuplicatedTransactionTest() {
        Transaction transaction =
                TransactionFaker.createValidTransaction();
        boolean isSaved =
                transactionMapper.saveTransaction(transaction, TABLE_NAME_SUFFIX);

        Assertions.assertTrue(isSaved);

        Assertions.assertThrows(DuplicateKeyException.class, () ->
                transactionMapper.saveTransaction(transaction, TABLE_NAME_SUFFIX)
        );
    }

    @Test
    public void saveTransactionWithEmptyInvalidParametersTest() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            transactionMapper.saveTransaction(
                    TransactionFaker.createEmptyTransactionIdTransaction(),
                    TABLE_NAME_SUFFIX
            );
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            transactionMapper.saveTransaction(
                    TransactionFaker.createEmptyAmountTransaction(),
                    TABLE_NAME_SUFFIX
            );
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            transactionMapper.saveTransaction(
                    TransactionFaker.createEmptyIbanTransaction(),
                    TABLE_NAME_SUFFIX
            );
        });
    }

    @Test
    public void getTransactionByTransactionIdTest() {
        Transaction transaction =
                TransactionFaker.createValidTransaction();
        transactionMapper.saveTransaction(transaction, TABLE_NAME_SUFFIX);

        Transaction savedTransaction =
                transactionMapper.getTransactionByTransactionId(
                        transaction.getTransactionId(),
                        TABLE_NAME_SUFFIX
                );

        Assertions.assertEquals(transaction, savedTransaction);
    }

    @Test
    public void getTransactionByInvalidTransactionIdTest() {
        Transaction transaction =
                TransactionFaker.createValidTransaction();
        transactionMapper.saveTransaction(transaction, TABLE_NAME_SUFFIX);

        Transaction savedTransaction =
                transactionMapper.getTransactionByTransactionId(
                        "invalid-transaction-id",
                        TABLE_NAME_SUFFIX
                );

        Assertions.assertNull(savedTransaction);
    }

    @Test
    public void getTransactionsByAccountIbanAndValueDateTest() {
        List<Transaction> transactions =
                transactionMapper.getTransactionsByAccountIbanAndValueDate(
                        "invalid-iban",
                        new Date(),
                        new Date(),
                        TABLE_NAME_SUFFIX
                );

        Assertions.assertEquals(0, transactions.size());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DATE, 2);
        Date endDate = calendar.getTime();

        Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                "valid-amount",
                "valid-iban",
                startDate,
                "description"
        );
        transactionMapper.saveTransaction(transaction, TABLE_NAME_SUFFIX);

        Transaction transactionUnderSameIban = new Transaction(
                UUID.randomUUID().toString(),
                "valid-amount",
                "valid-iban",
                endDate,
                "description"
        );
        transactionMapper.saveTransaction(
                transactionUnderSameIban,
                TABLE_NAME_SUFFIX
        );

        transactions =
                transactionMapper.getTransactionsByAccountIbanAndValueDate(
                        transaction.getAccountIban(),
                        startDate,
                        endDate,
                        TABLE_NAME_SUFFIX
                );

        Assertions.assertEquals(1, transactions.size());
    }
}
