package com.oliver.accountBackend.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A creating transaction submit form
 */
@ApiModel("A creating transaction submit form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTransactionForm {
    /**
     * Transaction's amount.
     */
    @ApiModelProperty(value = "Transaction's amount (e.g. 75)", required = true)
    private double amount;

    /**
     * Additional description of the transaction.
     */
    @ApiModelProperty("Additional description of the transaction")
    private String description;

    /**
     * Returns transaction's amount.
     * @return {String} Returns transaction's amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns description of the transaction.
     * @return {String} Returns description of the transaction.
     */
    public String getDescription() {
        return description;
    }
}
