package com.oliver.accountBackend.controller;

import com.oliver.accountBackend.form.CreateAccountForm;
import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.domain.Transaction;
import com.oliver.accountBackend.form.CreateTransactionForm;
import com.oliver.accountBackend.service.AccountService;
import com.oliver.accountBackend.service.AccountTransactionService;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.pagenation.Page;
import com.oliver.response.ResponseResult;
import com.oliver.response.StatusCode;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Slf4j
@RestController
@RequestMapping("/account")
@Api(tags = "Account Operations")
public class AccountController {
    private AccountService accountService;

    private AccountTransactionService accountTransactionService;

    /**
     * Attempts to create an account for the logged-in user.
     * The logged-in user will have a new role to access this account
     * if account is created successfully.
     *
     * @param createAccountForm {CreateAccountForm} A form includes country
     *                                              and iban (optional)
     *
     * @return {ResponseResult<Account>} Returns 'ResponseResult' with a
     *                                   newly-created `Account` if success,
     *                                   Otherwise will return `ResponseResult`
     *                                   with error message.
     */
    @ApiOperation(
            value = "Attempts to create an account for the logged-in user",
            notes = "The logged-in user will have a new role to access this account " +
                    "if account is created successfully.",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseResult.class
    )
    @ApiResponse(code = 200, message = "Request API Success", response = ResponseResult.class)
    @ApiImplicitParam(
            value = "JWT token with \"Bearer\" prefix",
            name = "Authorization",
            paramType = "header",
            dataType = "String",
            required = true
    )
    @PreAuthorize("hasAuthority('/account')")
    @PostMapping(value = "/createAccount", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResult<Account> createAccount(@RequestBody CreateAccountForm createAccountForm) {
        String country = createAccountForm.getCountry();
        String iban = createAccountForm.getIban();

        Account account;
        try {
            account = accountService.createAccount(country, iban);
        } catch (Exception e) {
            log.error("Cannot create account.", e);
            return new ResponseResult<>(StatusCode.ERROR, e.getMessage());
        }

        return new ResponseResult<>(StatusCode.OK, "Create account successfully", account);
    }

    /**
     * Attempts to create a transaction under given account.
     * and sends the transaction to kafka.
     * <p>
     * The newly created-transaction will not be saved to db.
     *
     * @param iban {int} Account's iban.
     * @param createTransactionForm {CreateTransactionForm} A form includes
     *                                                      amount and description
     *
     * @return {Account} Returns a newly-created `transaction` if success,
     *                   Otherwise will return null.
     * @throws ValidationException Throws ValidationException if account iban does not exist.
     * @throws ConflictException Throws ConflictException if transaction has been created.
     */
    @ApiOperation(
            value = "Attempts to create a transaction under given account.",
            notes = "Attempts to create a transaction under given account and " +
                    "sends the transaction to kafka.  The newly created-transaction " +
                    "will not be saved to db.",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseResult.class
    )
    @ApiResponse(code = 200, message = "Request API Success", response = ResponseResult.class)
    @ApiImplicitParam(
            value = "JWT token with \"Bearer\" prefix",
            name = "Authorization",
            paramType = "header",
            dataType = "String",
            required = true
    )
    @PostMapping(value = "/createTransaction/{iban}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('/account/' + #iban)")
    public ResponseResult<Transaction> createTransaction(
            @PathVariable("iban") String iban,
            @RequestBody CreateTransactionForm createTransactionForm
    ) {
        int amount = createTransactionForm.getAmount();
        String description = createTransactionForm.getDescription();

        Transaction transaction;
        try {
            transaction = accountTransactionService.createTransaction(amount, iban, description);
        } catch (ValidationException | ConflictException e) {
            log.error("Failed to create transaction with iban - {}", iban);
            log.error(e.getMessage());
            return new ResponseResult<>(StatusCode.ERROR, e.getMessage());
        }

        return new ResponseResult<>(
                StatusCode.OK,
                String.format(
                        "Create transaction successfully with iban - %s",
                        iban
                ),
                transaction
        );
    }

    /**
     * Attempts to retrieve a list of transactions by its account iban.
     * <p>
     * Returns a Page result with the total number of transactions
     * and the total credit and debit value of the current page
     * transactions.  Skips number of page number * page size transactions.
     *
     * @param iban {String} Transaction's account iban.
     * @param startDate {Date} Start date of transaction date.
     * @param endDate {Date} End date of transaction date.
     * @param pageNo {Integer} Page number.
     * @param pageSize {Integer} Page size.
     *
     * @return {Page<Transaction>>} Returns either a 'Page' Object representing the
     *                requested transactions.
     *
     * @throws ValidationException Throws ValidationException if account iban
     *                             does not exist.
     */
    @ApiOperation(
            value = "Attempts to retrieve a list of transactions by its account iban",
            notes = "Returns a Page result with the total number of transactions " +
                    "and the total credit and debit value of the current page " +
                    "transactions.  Skips number of page number * page size transactions.",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseResult.class
    )
    @ApiResponse(code = 200, message = "Request API Success", response = ResponseResult.class)
    @ApiImplicitParam(
            value = "JWT token with \"Bearer\" prefix",
            name = "Authorization",
            paramType = "header",
            dataType = "String",
            required = true
    )
    @GetMapping("/getTransactions/{iban}")
    @PreAuthorize("hasAuthority('/account/' + #iban)")
    public ResponseResult<Page<Transaction>> getTransactionsByIban(
            @PathVariable("iban") String iban,

            @ApiParam(value = "Start date of transaction date", required = true)
            @DateTimeFormat(pattern = "dd-MM-yyyy")
            @RequestParam("startDate")  Date startDate,

            @ApiParam(value = "Transaction's date", required = true)
            @DateTimeFormat(pattern = "dd-MM-yyyy")
            @RequestParam("endDate") Date endDate,

            @ApiParam(value = "Page number", required = true)
            @RequestParam("pageNo") Integer pageNo,

            @ApiParam(value = "Page size", required = true)
            @RequestParam("pageSize") Integer pageSize
    ) {
        Page<Transaction> transactionPage;
        try {
            transactionPage = accountTransactionService
                    .getTransactionsByAccountIbanAndValueDate(
                            iban,
                            startDate,
                            endDate,
                            pageNo,
                            pageSize
                    );
        } catch (ValidationException e) {
            log.error("Failed to get transactions with iban - {}", iban);
            log.error(e.getMessage());
            return new ResponseResult<>(StatusCode.ERROR, e.getMessage());
        }
        return new ResponseResult<>(
                StatusCode.OK,
                String.format(
                        "Fetch transactions for iban - %s",
                        iban
                ),
                transactionPage
        );
    }




    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setAccountTransactionService(AccountTransactionService accountTransactionService) {
        this.accountTransactionService = accountTransactionService;
    }


}
