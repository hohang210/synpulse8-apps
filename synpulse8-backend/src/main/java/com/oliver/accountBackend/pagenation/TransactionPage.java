package com.oliver.accountBackend.pagenation;

import com.oliver.accountBackend.domain.Transaction;
import com.oliver.pagenation.Page;

import java.io.Serializable;
import java.util.List;

public class TransactionPage implements Page<Transaction>, Serializable {

    private static final long serialVersionUID = -423920336750395165L;

    /**
     * Total number of transactions of current page.
     */
    private int total;

    /**
     * A list of transactions of current page.
     */
    private List<Transaction> transactions;

    /**
     * Total credit value of current page's transactions.
     */
    private int totalCredit = 0;

    /**
     * Total debit value of current page's transactions.
     */
    private int totalDebit = 0;

    public TransactionPage(List<Transaction> transactions) {
        this.transactions = transactions;
        this.total = transactions.size();

        calculateTotalCreditAndDebit(transactions);
    }

    /**
     * Calculate the total debit and credit of the given list of transactions.
     *
     * @param transactions {List<Transaction>} A list of transactions.
     */
    private void calculateTotalCreditAndDebit(List<Transaction> transactions) {
        transactions.forEach(transaction -> {
            String amountWithCurrency = transaction.getAmount();

            int amount = Integer.parseInt(amountWithCurrency.split(" ")[1]);
            if (amount > 0) {
                totalDebit += amount;
            } else {
                totalCredit += amount;
            }
        });
    }

    @Override
    public int getTotal() {
        return total;
    }

    @Override
    public List<Transaction> getData() {
        return transactions;
    }

    /**
     * Returns the total credit of current page's transactions.
     *
     * @return {int} Returns the total credit of current page's transactions.
     */
    public int getTotalCredit() {
        return totalCredit;
    }

    /**
     * Returns the total debit of current page's transactions.
     *
     * @return {int} Returns the total debit of current page's transactions.
     */
    public int getTotalDebit() {
        return totalDebit;
    }
}
