DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `systems_menus`;
DROP TABLE IF EXISTS `system_menus`;
DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `role_menus`;
DROP TABLE IF EXISTS `roles_menus`;

CREATE TABLE IF NOT EXISTS `users` (
    `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` varchar(50) NOT NULL UNIQUE,
    `password` blob NOT NULL,
    `type` varchar(10) NOT NULL COMMENT 'admin/agency/synpulse8User etc',
    `deleted` tinyint(1) DEFAULT 0 COMMENT 'A flag indicated whether user is deleted (1: deleted, 0: active)'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `roles` (
    `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(50) NOT NULL UNIQUE,
    `deleted` tinyint(1) DEFAULT 0 COMMENT 'A flag indicated whether role is deleted (1: deleted, 0: active)'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `system_menus` (
    `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `permission` varchar(10) NOT NULL COMMENT 'grant/deny',
    `resource` varchar(100) NOT NULL COMMENT 'Resource identifying which resources permission is being granted or denied to',
    `deleted` tinyint(1) DEFAULT 0 COMMENT 'A flag indicated whether menu is deleted (1: deleted, 0: active)',
    UNIQUE KEY resource_permission (`resource`, `permission`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users_roles` (
    `user_id` int(11) NOT NULL,
    `role_id` int(11) NOT NULL,
    PRIMARY KEY(`user_id`, `role_id`),
    INDEX users_roles_role_id (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `roles_menus` (
    `role_id` int(11) NOT NULL,
    `menu_id` int(11) NOT NULL,
    PRIMARY KEY(`role_id`, `menu_id`),
    INDEX roles_menus_menu_id (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;