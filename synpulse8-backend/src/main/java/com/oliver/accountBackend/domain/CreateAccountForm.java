package com.oliver.accountBackend.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A create account submit form for frontend.
 */
@ApiModel(description = "A creating account submit form")
public class CreateAccountForm {
    /**
     * Country of the account's currency type.
     */
    @ApiModelProperty(value = "Country of the account's currency type", required = true)
    private String country;

    /**
     * Account's iban.
     */
    @ApiModelProperty(value = "Account's iban")
    private String iban;

    /**
     * Returns country name of the account's currency type.
     * @return {String} Returns country name of the account's currency type.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns account's iban.
     * @return {String} Returns account's iban.
     */
    public String getIban() {
        return iban;
    }
}
