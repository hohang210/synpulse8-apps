package com.oliver.accountBackend.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliver.accountBackend.domain.Transaction;
import com.oliver.accountBackend.manager.AccountTransactionManager;
import com.oliver.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

@Slf4j
@RestController
public class TransactionListener {
    private static final String TOPIC = "test";

    private AccountTransactionManager accountTransactionManager;

    @Resource(name = "taskExecutor")
    private Executor taskExecutor;

    @KafkaListener(topics = TOPIC, groupId = "save-transaction")
    public void saveTransactions(ConsumerRecord<String, String> record) {
        taskExecutor.execute(() -> {
            String transactionJson = record.value();
            long offset = record.offset();

            try {
                Transaction transaction =
                        new ObjectMapper().readValue(transactionJson, Transaction.class);
                accountTransactionManager
                        .saveTransactionsToDBFromKafka(transaction, transactionJson);
            } catch (JsonProcessingException e) {
                log.error(
                        "Failed to parse transaction - {} to current transaction model, offset - {}",
                        transactionJson,
                        offset
                );
            } catch (ValidationException e) {
                log.error(
                        "Current model does not support given transaction - {}, offset - {}",
                        transactionJson,
                        offset
                );
            } catch (Exception e) {
                log.error(
                        "Failed to save transaction - {} to db, offset - {}",
                        transactionJson,
                        offset
                );
                log.error(e.getMessage());
            }
        });
    }

    @Autowired
    public void setAccountTransactionManager(AccountTransactionManager accountTransactionManager) {
        this.accountTransactionManager = accountTransactionManager;
    }

}
