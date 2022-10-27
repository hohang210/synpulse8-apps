package com.oliver.accountBackend.controller;

import com.oliver.accountBackend.domain.CreateAccountForm;
import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.domain.Transaction;
import com.oliver.accountBackend.service.AccountService;
import com.oliver.util.ResponseResult;
import com.oliver.util.StatusCode;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@Api(tags = "Account Operations")
public class AccountController {
    private AccountService accountService;

    /**
     * Attempts to create an account for the logged-in user.
     * The logged-in user will have a new role to access this account
     * if account is created successfully.
     *
     * @param createAccountForm {CreateAccountForm} A form retrieved from frontend, which
     *                                     includes country and iban (optional)
     *
     * @return {ResponseResult<Account>} Returns 'ResponseResult' with a
     *                                   newly-created `Account` if success,
     *                                   Otherwise will return `ResponseResult`
     *                                   with error message.
     */
    @ApiOperation(
            value = "Attempts to create an account for the logged-in user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseResult.class
    )
    @ApiResponses({
            @ApiResponse(code = StatusCode.OK, message = "Create account successfully"),
            @ApiResponse(code = StatusCode.ERROR, message = "Failed to create account")
    })
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
            return new ResponseResult<>(StatusCode.ERROR, e.getMessage());
        }

        return new ResponseResult<>(StatusCode.OK, "Create account successfully", account);
    }

    @GetMapping("/getTransaction/{iban}")
    @PreAuthorize("hasAuthority('/account/' + #iban)")
    public ResponseResult<List<Transaction>> getTransactionsByIban(@PathVariable("iban") String iban) {
        System.out.println(iban);
        return null;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
