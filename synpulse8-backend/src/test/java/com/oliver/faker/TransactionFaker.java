package com.oliver.faker;

import com.github.javafaker.Faker;
import com.oliver.accountBackend.domain.Transaction;

import java.util.UUID;

public class TransactionFaker {
    private static final Faker faker = new Faker();

    private static final String amount = "valid-amount";

    /**
     * Fake a transaction.
     * @return {Transaction} Returns a fake transaction.
     */
    public static Transaction createValidTransaction() {
        return new Transaction(
                UUID.randomUUID().toString(),
                amount,
                faker.finance().iban(),
                faker.date().birthday(),
                faker.random().hex()
        );
    }

    /**
     * Fake an empty iban transaction.
     * @return {Transaction} Returns a fake empty iban transaction.
     */
    public static Transaction createTransactionWithSameIBAN(Transaction transaction) {
        return new Transaction(
                UUID.randomUUID().toString(),
                amount,
                transaction.getAccountIban(),
                faker.date().birthday(),
                faker.random().hex()
        );
    }

    /**
     * Fake an empty transactionId transaction.
     * @return {Transaction} Returns a fake empty transactionId transaction.
     */
    public static Transaction createEmptyTransactionIdTransaction() {
        return new Transaction(
                null,
                amount,
                faker.finance().iban(),
                faker.date().birthday(),
                faker.random().hex()
        );
    }

    /**
     * Fake an empty amount transaction.
     * @return {Transaction} Returns a fake amount iban transaction.
     */
    public static Transaction createEmptyAmountTransaction() {
        return new Transaction(
                UUID.randomUUID().toString(),
                null,
                faker.finance().iban(),
                faker.date().birthday(),
                faker.random().hex()
        );
    }

    /**
     * Fake an empty iban transaction.
     * @return {Transaction} Returns a fake empty iban transaction.
     */
    public static Transaction createEmptyIbanTransaction() {
        return new Transaction(
                UUID.randomUUID().toString(),
                amount,
                null,
                faker.date().birthday(),
                faker.random().hex()
        );
    }

    /**
     * Fake an empty iban transaction.
     * @return {Transaction} Returns a fake empty iban transaction.
     */
    public static Transaction createEmptyValueDateTransaction() {
        return new Transaction(
                UUID.randomUUID().toString(),
                amount,
                faker.finance().iban(),
                null,
                faker.random().hex()
        );
    }

    /**
     * Fake an empty description transaction.
     * @return {Transaction} Returns a fake empty description transaction.
     */
    public static Transaction createEmptyDescriptionTransaction() {
        return new Transaction(
                UUID.randomUUID().toString(),
                amount,
                faker.finance().iban(),
                faker.date().birthday(),
                null
        );
    }
}
