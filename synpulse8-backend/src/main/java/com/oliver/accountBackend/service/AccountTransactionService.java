package com.oliver.accountBackend.service;

import com.oliver.accountBackend.domain.Transaction;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.pagenation.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public interface AccountTransactionService {
    /**
     * Attempts to create a transaction under given account
     * and sends the transaction to kafka.
     * <p>
     * The newly created-transaction will not be saved to db.
     * <p>
     * This function does not have the functionality to transfer money
     * from one account to another account.  This function is only used for
     * create a fake transaction for testing usage.
     *
     * @param amount {int} Transaction's amount.
     * @param accountIban {String} Account's iban.
     * @param description {String} Additional description of the transaction.
     *
     * @return {Transaction} Returns a newly-created `transaction` if success,
     *                   Otherwise will return null.
     * @throws ValidationException Throws ValidationException if account iban does not exist.
     * @throws ConflictException Throws ConflictException if transaction has been created.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Transaction createTransaction(
            int amount,
            String accountIban,
            String description
    ) throws ValidationException, ConflictException;

    /**
     * Attempts to retrieve a list of transactions by its account iban.
     * Will return a Page result with the total number of transactions
     * and the total credit and debit value of the current page
     * transactions.  Skips number of page number * page size transactions.
     *
     * @param accountIban {String} Transaction's account iban.
     * @param startDate {Date} Start date of transaction date.
     * @param endDate {Date} End date of transaction date.
     * @param pageNo {int} Page number.
     * @param pageSize {int} Page size.
     *
     * @return {Page<Transaction>>} Returns either a 'Page' Object representing the
     *                requested transactions.
     *
     * @throws ValidationException Throws ValidationException if account iban
     *                             does not exist.
     */
    Page<Transaction> getTransactionsByAccountIbanAndValueDate(
            String accountIban,
            Date startDate,
            Date endDate,
            Integer pageNo,
            Integer pageSize
    ) throws ValidationException;
}
