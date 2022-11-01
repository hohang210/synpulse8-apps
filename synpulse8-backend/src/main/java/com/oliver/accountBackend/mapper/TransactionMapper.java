package com.oliver.accountBackend.mapper;

import com.oliver.accountBackend.domain.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TransactionMapper {
    /**
     * Create a transaction table on database
     * if 'transactions' + tableNameSuffix does not exist.
     * The default table name will be 'transactions'
     *
     * @param tableNameSuffix A unique suffix string of table name.
     *                        (e.g. 2011_03_1)
     *                        - 2011 representing the year of transaction
     *                        - 03 representing the month of transaction
     *                        - 1 representing the reminder of
     *                          hash value of transaction id divided by
     *                          5000
     *
     */
    void createTransactionTable(@Param("tableNameSuffix") String tableNameSuffix);

    /**
     * Attempts to save the given transaction to db.
     *
     * @param transaction {Transaction} An transaction to save.
     * @param tableNameSuffix A unique suffix string of table name.
     *                        (e.g. 2011_03_1)
     *                        - 2011 representing the year of transaction
     *                        - 03 representing the month of transaction
     *                        - 1 representing the reminder of
     *                          hash value of transaction id divided by
     *                          5000
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   given transaction is saved.
     */
    boolean saveTransaction(
            @Param("transaction") Transaction transaction,
            @Param("tableNameSuffix") String tableNameSuffix
    );

    /**
     * Attempts to retrieve a transaction by its unique identifier.
     *
     * @param transactionId {String} Transaction's id.
     * @param tableNameSuffix A unique suffix string of table name.
     *                        (e.g. 2011_03_1)
     *                        - 2011 representing the year of transaction
     *                        - 03 representing the month of transaction
     *                        - 1 representing the reminder of
     *                          hash value of transaction id divided by
     *                          5000
     *
     * @return {Transaction} Returns either a 'Transaction' Object representing the
     *                requested transaction or 'null' if the ID could not be
     *                found.
     */
    Transaction getTransactionByTransactionId(
            @Param("transactionId") String transactionId,
            @Param("tableNameSuffix") String tableNameSuffix
    );

    /**
     * Attempts to retrieve a transaction by its account iban.
     *
     * @param iban {String} Transaction's account iban.
     * @param startDate {Date} Start date of transaction date.
     * @param endDate {Date} End date of transaction date.
     * @param tableNameSuffix A unique suffix string of table name.
     *                        (e.g. 2011_03_1)
     *                        - 2011 representing the year of transaction
     *                        - 03 representing the month of transaction
     *                        - 1 representing the reminder of
     *                          hash value of transaction id divided by
     *                          5000
     *
     *
     * @return {List<Transaction>} Returns either a 'Transaction' Object representing the
     *                requested transaction or 'null' if the ID could not be
     *                found.
     */
    List<Transaction> getTransactionsByAccountIbanAndValueDate(
            @Param("iban") String iban,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("tableNameSuffix") String tableNameSuffix
    );

    /**
     * Drop transaction table.
     *
     * @param tableNameSuffix A unique suffix string of table name.
     *                        (e.g. 2011_03_1)
     *                        - 2011 representing the year of transaction
     *                        - 03 representing the month of transaction
     *                        - 1 representing the reminder of
     *                          hash value of transaction id divided by
     *                          5000
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   the transaction table are removed.
     */
    void dropTransactionTable(@Param("tableNameSuffix") String tableNameSuffix);
}
