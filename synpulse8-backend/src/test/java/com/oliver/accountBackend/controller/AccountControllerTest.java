package com.oliver.accountBackend.controller;

import com.alibaba.fastjson.JSON;
import com.oliver.Synpulse8BackendApplication;
import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.domain.Transaction;
import com.oliver.accountBackend.manager.AccountManager;
import com.oliver.accountBackend.manager.AccountTransactionManager;
import com.oliver.accountBackend.mapper.AccountMapper;
import com.oliver.accountBackend.mapper.TransactionMapper;
import com.oliver.accountBackend.mapper.UserAccountMapper;
import com.oliver.accountBackend.pagenation.TransactionPage;
import com.oliver.apiGateway.service.UserService;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.faker.AccountFaker;
import com.oliver.faker.TransactionFaker;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.*;
import com.oliver.response.ResponseResult;
import com.oliver.response.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest(
        classes = Synpulse8BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ActiveProfiles("test")
public class AccountControllerTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private AccountTransactionManager accountTransactionManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private RestTemplate restTemplate;

    @Value("http://localhost:${server.port}")
    private String baseUrl;

    private String jwt;

    @BeforeEach
    public void setUp() throws ValidationException, ConflictException {
        User user = UserFaker.createValidUser();
        String username = user.getUsername();
        String password = user.getPassword();

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);

        userService.createUserWithBasicRole(
                username,
                password
        );

        String url = baseUrl + "/login";
        ResponseResult<Map<String, String>> responseResult =
                restTemplate.postForEntity(
                        url,
                        request,
                        ResponseResult.class
                ).getBody();

        jwt = responseResult.getData().get("jwt");
    }

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
        accountMapper.removeAllAccountsFromDB();
        userAccountMapper.removeAllUsersAccountsFromDB();
    }


    @Test
    public void createAccountTest() {
        Account account = AccountFaker.createValidAccount();

        Map<String, String> request = new HashMap<>();
        request.put("country", "Canada");
        request.put("iban", account.getIban());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestParam = new HttpEntity<>(JSON.toJSONString(request), headers);

        String createAccountURL = baseUrl + "/account/createAccount";
        ResponseResult<Account> responseResult =
                restTemplate.postForEntity(
                        createAccountURL,
                        requestParam,
                        ResponseResult.class
                ).getBody();

        Assertions.assertEquals(StatusCode.OK, responseResult.getCode());
        Assertions.assertEquals(
                "Create account successfully",
                responseResult.getMessage()
        );
    }

    @Test
    public void createAccountTestWithUnauthenticatedUser() {
        Account account = AccountFaker.createValidAccount();

        Map<String, String> request = new HashMap<>();
        request.put("country", "Canada");
        request.put("iban", account.getIban());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "stupid-jwt");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestParam = new HttpEntity<>(JSON.toJSONString(request), headers);

        String createAccountURL = baseUrl + "/account/createAccount";
        ResponseResult<Account> responseResult =
                restTemplate.postForEntity(
                        createAccountURL,
                        requestParam,
                        ResponseResult.class
                ).getBody();

        Assertions.assertEquals(StatusCode.LOGIN_ERROR, responseResult.getCode());
        Assertions.assertEquals(
                "Full authentication is required to access this resource",
                responseResult.getMessage()
        );
    }

    @Test
    public void createAccountTestWithUnauthorizedUser() throws ValidationException, ConflictException {
        User user = UserFaker.createValidUser();
        String username = user.getUsername();
        String password = user.getPassword();

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        userService.createUserWithBasicRole(
                username,
                password
        );

        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();

        String url = baseUrl + "/login";
        ResponseResult<Map<String, String>> loginResponseResult =
                restTemplate.postForEntity(
                        url,
                        loginRequest,
                        ResponseResult.class
                ).getBody();

        jwt = loginResponseResult.getData().get("jwt");

        Account account = AccountFaker.createValidAccount();

        Map<String, String> request = new HashMap<>();
        request.put("country", "Canada");
        request.put("iban", account.getIban());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestParam = new HttpEntity<>(JSON.toJSONString(request), headers);

        String createAccountURL = baseUrl + "/account/createAccount";
        ResponseResult<Account> responseResult =
                restTemplate.postForEntity(
                        createAccountURL,
                        requestParam,
                        ResponseResult.class
                ).getBody();

        Assertions.assertEquals(StatusCode.ACCESS_ERROR, responseResult.getCode());
        Assertions.assertEquals(
                "Unauthorized",
                responseResult.getMessage()
        );
    }

    @Test
    public void getTransactionsByAccountIbanAndValueDate() throws ValidationException, ConflictException {
        Transaction fakeTransaction = TransactionFaker.createValidTransaction();

        Map<String, String> request = new HashMap<>();
        request.put("country", "Canada");
        request.put("iban", fakeTransaction.getAccountIban());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestParam = new HttpEntity<>(JSON.toJSONString(request), headers);

        String createAccountURL = baseUrl + "/account/createAccount";
        restTemplate.postForEntity(
                createAccountURL,
                requestParam,
                ResponseResult.class
        ).getBody();

        String tableNameSuffix =
                accountTransactionManager
                        .getTransactionTableNameSuffix(
                                fakeTransaction.getAccountIban()
                        );

        String validIBANTableNameSuffix =
                accountTransactionManager
                        .getTransactionTableNameSuffix(
                                "valid-account-iban"
                        );


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date startDate = calendar.getTime();
        calendar.add(2, Calendar.MINUTE);

        Transaction transaction1 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD -100",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description-1"
        );

        calendar.add(Calendar.DATE, 1);
        Transaction transaction2 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD 100",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description-2"
        );

        Transaction transaction3 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD 100",
                "valid-account-iban",
                calendar.getTime(),
                "description-3"
        );

        calendar.add(Calendar.DATE, 1);
        Transaction transaction4 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD 100",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description-4"
        );

        Transaction transaction5 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD 100",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description-5"
        );

        Date endDate = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Transaction transaction6 = new Transaction(
                UUID.randomUUID().toString(),
                "CAD 100",
                fakeTransaction.getAccountIban(),
                calendar.getTime(),
                "description-6"
        );

        transactionMapper.createTransactionTable(tableNameSuffix);
        transactionMapper.createTransactionTable(validIBANTableNameSuffix);
        transactionMapper.saveTransaction(transaction1, tableNameSuffix);
        transactionMapper.saveTransaction(transaction2, tableNameSuffix);
        transactionMapper.saveTransaction(transaction3, validIBANTableNameSuffix);
        transactionMapper.saveTransaction(transaction4, tableNameSuffix);
        transactionMapper.saveTransaction(transaction5, tableNameSuffix);
        transactionMapper.saveTransaction(transaction6, tableNameSuffix);

//        localhost:8889/account/getTransaction/valid-iban?startDate=30-10-2022&endDate=31-10-2022&pageNo=1&pageSize=10

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String startDateStr = simpleDateFormat.format(startDate);
        String endDateStr = simpleDateFormat.format(endDate);

        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append("/account/getTransactions/");
        sb.append(fakeTransaction.getAccountIban());
        sb.append("?startDate=");
        sb.append(startDateStr);
        sb.append("&endDate=");
        sb.append(endDateStr);
        sb.append("&pageNo=1&pageSize=10");
        System.out.println(sb.toString());

        HttpHeaders getTransactionsHeaders = new HttpHeaders();
        getTransactionsHeaders.add("Authorization", jwt);
        getTransactionsHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> getTransactionsRequest = new HttpEntity<>(null, getTransactionsHeaders);

        ResponseEntity<ResponseResult> exchange = restTemplate.exchange(
                sb.toString(),
                HttpMethod.GET,
                getTransactionsRequest,
                ResponseResult.class
        );

        TransactionPage transactionPage = (TransactionPage) exchange.getBody().getData();
        List<Transaction> transactions = transactionPage.getData();

        int debit = transactionPage.getTotalDebit();
        int credit = transactionPage.getTotalCredit();

        Assertions.assertEquals(300, debit);
        Assertions.assertEquals(-100, credit);

        Assertions.assertEquals(4, transactions.size());
        Assertions.assertTrue(transactions.contains(transaction1));
        Assertions.assertTrue(transactions.contains(transaction2));
        Assertions.assertTrue(transactions.contains(transaction4));
        Assertions.assertTrue(transactions.contains(transaction5));


        transactionMapper.dropTransactionTable(tableNameSuffix);
        transactionMapper.dropTransactionTable(validIBANTableNameSuffix);
    }
}
