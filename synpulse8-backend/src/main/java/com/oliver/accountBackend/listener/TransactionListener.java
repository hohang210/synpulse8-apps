package com.oliver.accountBackend.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliver.accountBackend.domain.Transaction;
import com.oliver.accountBackend.manager.AccountTransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

@Slf4j
@RestController
public class TransactionListener {
    private AccountTransactionManager accountTransactionManager;

    private DataSourceTransactionManager transactionManager;

    @Resource(name = "taskExecutor")
    private Executor taskExecutor;

    @KafkaListener(topics = "#{'${kafka-topic}'}", groupId = "save-transaction")
    public void saveTransactions(List<ConsumerRecord<String, String>> records) throws InterruptedException {
        if (records.isEmpty()) {
            return;
        }

        int messagesSize = records.size();
        CountDownLatch countDownLatch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            int startPosition = i * 200;
            int endPosition = (i + 1) * 200;

            taskExecutor.execute(() -> {
                if (startPosition <= messagesSize) {
                    DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
                    definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionStatus transactionStatus = transactionManager.getTransaction(definition);

                    for (int j = startPosition; j < Math.min(endPosition, messagesSize); j++) {
                        ConsumerRecord<String, String> record = records.get(j);
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
                        } catch (Exception e) {
                            log.error(
                                    "Failed to save transaction - {} to db, offset - {}",
                                    transactionJson,
                                    offset
                            );
                            log.error(e.getMessage());
                        }
                    }

                    transactionManager.commit(transactionStatus);
                }

                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
    }

    @Autowired
    public void setAccountTransactionManager(AccountTransactionManager accountTransactionManager) {
        this.accountTransactionManager = accountTransactionManager;
    }

    @Autowired
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.transactionManager = dataSourceTransactionManager;
    }
}
