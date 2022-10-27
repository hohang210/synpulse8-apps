package com.oliver.accountBackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Objects;

@Alias("Account")
@Slf4j
@ApiModel
public class Account implements Serializable {
    private static final long serialVersionUID = -6601129638412508104L;

    /**
     * Account's unique identifier.
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer id;

    /**
     * Account's iban
     */
    @ApiModelProperty(value = "Account's iban", required = true)
    private String iban;

    /**
     * Account's currency type
     */
    @ApiModelProperty(value = "Account's currency type", required = true)
    private String currency;

    /**
     * A flag indicated whether account is deleted (1: deleted, 0: active)
     * Default value is false
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private boolean deleted = false;

    public Account() {}

    /**
     * Generates an account based on the iban and currency.
     *
     * @param iban {String} Account's iban.
     * @param currency {String} Account's currency type.
     */
    public Account(String iban, String currency) {
        this.iban = iban;
        this.currency = currency;
    }

    /**
     * Returns account's unique identifier.
     *
     * @return {String} Returns account's unique identifier.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns account's iban.
     *
     * @return {String} Returns account's iban.
     */
    public String getIban() {
        return iban;
    }

    /**
     * Returns account's currency type.
     *
     * @return {String} Returns account's currency type.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns a flag to indicated whether account is deleted.
     *
     * @return {boolean} Returns a flag to indicated whether account is deleted.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Returns a string of current account data.
     * @return {String} Returns a string of current account data.
     */
    @Override
    public String toString() {
        return "Synpulse8Account{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", currency='" + currency + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    /**
     * Compares between the given object and current account.
     * Returns a flag to indicate whether they are equal.
     *
     * @param o {Object} An 'Account' object.
     * @return {boolean} Returns a flag to indicate whether
     *                   current account is equal to given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account that = (Account) o;
        return id.equals(that.id);
    }

    /**
     * Uses object.hash() to hash current account.
     *
     * @return {int} Returns a hashcode of current account.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
