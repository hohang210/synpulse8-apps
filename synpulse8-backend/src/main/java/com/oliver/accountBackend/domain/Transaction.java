package com.oliver.accountBackend.domain;

import java.util.Date;

public interface Transaction {
    String transactionId = null;

    String amount = null;

    String accountIban = null;

    Date valueDate = new Date();

    String description = null;
}
