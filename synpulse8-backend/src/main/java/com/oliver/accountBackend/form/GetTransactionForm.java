package com.oliver.accountBackend.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * A fetching transactions form
 */
@ApiModel("A fetching transactions submit form")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTransactionForm {
    /**
     * Start date of transactions.
     */
    @ApiModelProperty(
            value = "Start date of transaction date",
            example = "30-10-2022",
            required = true
    )
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date startDate;

    /**
     * End date of transactions.
     */
    @ApiModelProperty(
            value = "End date of transaction date",
            example = "30-10-2022",
            required = true
    )
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;

    /**
     * Page number.
     */
    @ApiModelProperty(value = "Page number", required = true)
    private Integer pageNo;

    /**
     * Page size.
     */
    @ApiModelProperty(value = "Page size", required = true)
    private Integer pageSize;

    /**
     * Returns start date of transactions.
     * @return {Date} Returns start date of transactions.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Returns end date of transactions.
     * @return {Date} Returns end date of transactions.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Returns page number.
     * @return {Integer} Returns page number.
     */
    public Integer getPageNo() {
        return pageNo;
    }

    /**
     * Returns page size.
     * @return {Integer} Returns page size.
     */
    public Integer getPageSize() {
        return pageSize;
    }
}
