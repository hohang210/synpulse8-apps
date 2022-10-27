DROP TABLE IF EXISTS `accounts`;
DROP TABLE IF EXISTS `users_accounts`;
DROP TABLE IF EXISTS `transactions`;

CREATE TABLE IF NOT EXISTS `accounts` (
    `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `iban` varchar(50) NOT NULL UNIQUE,
    `currency` char(10) NOT NULL,
    `deleted` tinyint(1) DEFAULT 0 COMMENT 'A flag indicated whether account is deleted (1: deleted, 0: active)'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users_accounts` (
    `user_id` int(11) NOT NULL,
    `account_id` int(11) NOT NULL,
    PRIMARY KEY(`user_id`, `account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `transactions` (
    `transaction_id` varchar(30) NOT NULL UNIQUE COMMENT 'UUID of the transaction',
    `amount` char(10) NOT NULL,
    `account_iban` varchar(30) NOT NULL,
    `value_date` TIMESTAMP NOT NULL,
    `description` TINYTEXT,
    PRIMARY KEY(`account_iban`, `transaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;