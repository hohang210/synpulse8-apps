package com.oliver.accountBackend.service;

import com.oliver.accountBackend.domain.Transaction;
import com.oliver.accountBackend.manager.AccountTransactionManager;
import com.oliver.accountBackend.mapper.TransactionMapper;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.pagenation.Page;
import com.oliver.accountBackend.pagenation.TransactionPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
class AccountTransactionServiceImpl implements AccountTransactionService {
    private AccountTransactionManager accountTransactionManager;

    @Override
    public Transaction createTransaction(
            int amount,
            String accountIban,
            String description
    ) throws ValidationException, ConflictException {
        return accountTransactionManager.createTransaction(
                null,
                amount,
                accountIban,
                null,
                description
        );
    }

    @Override
    public Page<Transaction> getTransactionsByAccountIbanAndValueDate(
            String accountIban,
            Date startDate,
            Date endDate,
            Integer pageNo,
            Integer pageSize
    ) throws ValidationException {
        List<Transaction> transactions =
                accountTransactionManager
                        .getTransactionsByAccountIbanAndValueDate(
                                accountIban,
                                startDate,
                                endDate,
                                pageNo,
                                pageSize
                        );

        return new TransactionPage(transactions);
    }

    @Autowired
    public void setAccountTransactionManager(AccountTransactionManager accountTransactionManager) {
        this.accountTransactionManager = accountTransactionManager;
    }
}
