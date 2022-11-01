package com.oliver.accountBackend.manager;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.domain.Transaction;
import com.oliver.accountBackend.mapper.AccountMapper;
import com.oliver.accountBackend.mapper.TransactionMapper;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.pagenation.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.oliver.accountBackend.configuration.EnvironmentConstants.KAFKA_TOPIC;

@Slf4j
@Service
public class AccountTransactionManager {
    private TransactionMapper transactionMapper;

    private AccountMapper accountMapper;

    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Attempts to create a transaction under given account
     * and sends the transaction to kafka.
     * <p>
     * The newly created-transaction will not be saved to db.
     *
     * @param transactionId {String} Transaction's unique identifier (Optional).
     * @param amount {Integer} Transaction's amount.
     * @param accountIban {String} Account's iban.
     * @param valueDate {Date} Transaction's date (Optional).
     * @param description {String} Additional description of the transaction.
     *
     * @return {Account} Returns a newly-created `transaction` if success,
     *                   Otherwise will return null.
     */
    @Transactional
    public Transaction createTransaction(
            String transactionId,
            Integer amount,
            String accountIban,
            Date valueDate,
            String description
    ) throws ValidationException, ConflictException {
        if (amount == null) {
            log.error("Amount cannot be empty when try to create transaction.");
            throw new ValidationException(
                    "Amount cannot be empty when try to create transaction."
            );
        }

        if (accountIban == null) {
            log.error("Account IBAN cannot be empty when try to create transaction.");
            throw new ValidationException(
                    "Account IBAN cannot be empty when try to create transaction."
            );
        }

        // Create a random UUID as a transaction id if not provided
        if (transactionId == null) {
            transactionId = UUID.randomUUID().toString();
        }

        if (valueDate == null) {
            valueDate = new Date();
        }

        Account account = accountMapper.getAccountByIban(accountIban);
        if (account == null) {
            log.error("Account iban {} does not exist", accountIban);
            throw new ValidationException(
                    String.format(
                            "Account iban %s does not exist",
                            accountIban
                    )
            );
        }

        String tableNameSuffix =
                getTransactionTableNameSuffix(accountIban);
        log.debug("Created transaction table name suffix {}", tableNameSuffix);
        transactionMapper.createTransactionTable(tableNameSuffix);

        Transaction transaction =
                transactionMapper.getTransactionByTransactionId(
                        transactionId,
                        tableNameSuffix
                );
        if (transaction != null) {
            log.error("Transaction {} has been saved to db", transactionId);
            throw new ConflictException(
                    "Transaction id",
                    String.format(
                            "Transaction %s has been saved to db",
                            transactionId
                    )
            );
        }

        transaction = new Transaction(
                transactionId,
                account.getCurrency() + " " + amount,
                accountIban,
                valueDate,
                description
        );

        kafkaTemplate.send(KAFKA_TOPIC, transactionId, JSON.toJSONString(transaction));
        return transaction;
    }

    /**
     * Attempts to retrieve a list of transactions by its account iban
     * between start date and end date.  Skips number of
     * page number * page size transactions.
     *
     * @param accountIban {String} Transaction's account iban.
     * @param startDate {Date} Start date of transaction date.
     * @param endDate {Date} End date of transaction date.
     * @param pageNo {Integer} Page number.
     * @param pageSize {Integer} Page size.
     *
     * @return {List<Transaction>} Returns either a 'Transaction' Object representing the
     *                requested transaction or 'null' if the ID could not be
     *                found.
     */
    public List<Transaction> getTransactionsByAccountIbanAndValueDate(
            String accountIban,
            Date startDate,
            Date endDate,
            Integer pageNo,
            Integer pageSize
    ) throws ValidationException {
        if (accountIban == null) {
            log.error("Account IBAN cannot be empty when try to get transactions for the account.");
            throw new ValidationException(
                    "Account IBAN cannot be empty when try to get transactions for the account."
            );
        }

        if (startDate == null) {
            log.error("Start date cannot be empty when try to get transactions for the account.");
            throw new ValidationException(
                    "Start date cannot be empty when try to get transactions for the account."
            );
        }

        if (endDate == null) {
            log.error("End date cannot be empty when try to get transactions for the account.");
            throw new ValidationException(
                    "End date cannot be empty when try to get transactions for the account."
            );
        }

        if (accountMapper.getAccountByIban(accountIban) == null) {
            log.error("Account iban {} does not exist", accountIban);
            throw new ValidationException(
                    String.format(
                            "Account iban %s does not exist",
                            accountIban
                    )
            );
        }

        String tableNameSuffix =
                getTransactionTableNameSuffix(accountIban);
        transactionMapper.createTransactionTable(tableNameSuffix);

        pageNo = PageUtil.getPageNo(pageNo);
        pageSize = PageUtil.getPageSize(pageSize);

        PageHelper.startPage(pageNo, pageSize);
        return transactionMapper
                .getTransactionsByAccountIbanAndValueDate(
                        accountIban,
                        startDate,
                        endDate,
                        tableNameSuffix
                        );
    }

    /**
     * Attempts to save the kafka's transactions to db.
     *
     * @param transaction {Transaction} A transaction Object parsed from the json received from kafka.
     * @param transactionJson {String} A transaction json string received from kafka.
     * @throws ValidationException Throws ValidationException if current data model
     *                             does not support current transaction.
     */
    public void saveTransactionsToDBFromKafka(
            Transaction transaction,
            String transactionJson
    ) {
        String accountIban = transaction.getAccountIban();
        String transactionId = transaction.getTransactionId();
        String amount = transaction.getAmount();

        if (accountIban == null || transactionId == null || amount == null) {
            throw new ValidationException(
                    String.format(
                            "Current model does not support given transaction - %s",
                            transactionJson
                    )
            );
        }

        String tableNameSuffix = getTransactionTableNameSuffix(accountIban);

        Transaction consumedTransaction =
                transactionMapper
                        .getTransactionByTransactionId(
                                transactionId,
                                tableNameSuffix
                        );
        if (consumedTransaction != null) {
            throw new ConflictException(
                    "transactionId",
                    String.format(
                            "Transaction - %s has been consumed",
                            transactionId
                    )
            );
        }

        transactionMapper.saveTransaction(transaction, tableNameSuffix);
    }

    /**
     * Create transaction table name's suffix
     * based on the iban.
     *
     * @param accountIban {String} Account's iban.
     *
     * @return {String} Returns a transaction table name's suffix
     *                  based on the value date of the transaction.
     */
    public String getTransactionTableNameSuffix(String accountIban) {
        int hashCode = Math.abs(Objects.hash(accountIban) % 5000);
        return String.valueOf(hashCode);
    }

    @Autowired
    public void setTransactionMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    @Autowired
    public void setAccountMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
