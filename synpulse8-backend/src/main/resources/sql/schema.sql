CREATE TABLE IF NOT EXISTS `accounts` (
    `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `iban` varchar(50) NOT NULL UNIQUE,
    `currency` char(10) NOT NULL,
    `deleted` tinyint(1) DEFAULT 0 COMMENT 'A flag indicated whether account is deleted (1: deleted, 0: active)'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users_accounts` (
    `user_id` int(11) NOT NULL,
    `account_id` int(11) NOT NULL,
    PRIMARY KEY(`user_id`, `account_id`),
    INDEX users_accounts_account_id (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
