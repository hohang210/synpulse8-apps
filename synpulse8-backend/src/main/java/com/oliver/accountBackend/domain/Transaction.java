package com.oliver.accountBackend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@ApiModel
@Alias("Transaction")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {
    private static final long serialVersionUID = -1670351294715474487L;

    /**
     * Transaction's unique identifier.
     */
    @ApiModelProperty(value = "Transaction's unique identifier", required = true)
    private String transactionId;

    /**
     * Transaction's amount with currency.
     * (e.g. CHF 75)
     */
    @ApiModelProperty(value = "Transaction's amount with currency (e.g. CHF 75)", required = true)
    private String amount;

    /**
     * Account's iban.
     */
    @ApiModelProperty(value = "Account's IBAN", required = true)
    private String accountIban;

    /**
     * Transaction's date.
     * (e.g. 29-10-2022)
     */
    @ApiModelProperty(value = "Transaction's date", example = "30-10-2022", required = true)
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "GMT+8")
    private Date valueDate;

    /**
     * Additional description of the transaction.
     */
    @ApiModelProperty("Additional description of the transaction")
    private String description;

    /**
     * Non parameters' constructor.
     */
    public Transaction() {}

    /**
     * Generates a transaction based on its amount, accountIban, valueDate and description
     *
     * @param transactionId {String} Transaction's unique identifier.
     * @param amount {String} Transaction's amount with currency.
     * @param accountIban {String} Account's iban.
     * @param valueDate {String} Transaction's date.
     * @param description {String} Additional description of the transaction.
     */
    public Transaction(
            String transactionId,
            String amount,
            String accountIban,
            Date valueDate,
            String description
    ) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.accountIban = accountIban;
        this.valueDate = valueDate;
        this.description = description;
    }

    /**
     * Returns transaction's unique identifier.
     * @return {String} Returns transaction's unique identifier.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Returns transaction's amount with currency.
     * @return {String} Returns transaction's amount with currency.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Returns transaction's account iban.
     * @return {String} Returns transaction's account iban.
     */
    public String getAccountIban() {
        return accountIban;
    }

    /**
     * Returns transaction's date.
     * @return {String} Returns transaction's date.
     */
    public Date getValueDate() {
        return valueDate;
    }

    /**
     * Returns description of the transaction.
     * @return {String} Returns description of the transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string of current transaction data.
     * @return {String} Returns a string of current transaction data.
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", amount='" + amount + '\'' +
                ", accountIban='" + accountIban + '\'' +
                ", valueDate=" + valueDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * Compares between the given object and current transaction.
     * Returns a flag to indicate whether they are equal.
     *
     * @param o {Object} An 'Transaction' object.
     * @return {boolean} Returns a flag to indicate whether
     *                   current transaction is equal to given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId.equals(that.transactionId) && accountIban.equals(that.accountIban);
    }

    /**
     * Uses object.hash() to hash current transaction.
     *
     * @return {int} Returns a hashcode of current transaction.
     */
    @Override
    public int hashCode() {
        return Objects.hash(transactionId, accountIban);
    }
}
