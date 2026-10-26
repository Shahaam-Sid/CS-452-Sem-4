CREATE TABLE `users` (
  `user_id` binary(16) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `dob` date NOT NULL,
  `country` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `groups` (
  `group_id` binary(16) NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `currency` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by` binary(16) NOT NULL,
  `created_at` timestamp NOT NULL,
  PRIMARY KEY (`group_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE `expenses` (
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL,
  `expense_id` binary(16) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `group_id` binary(16) NOT NULL,
  `paid_by` binary(16) NOT NULL,
  `split_type` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by` binary(16) NOT NULL,
  PRIMARY KEY (`expense_id`),
  KEY `group_id` (`group_id`),
  KEY `paid_by` (`paid_by`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `expenses_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`group_id`),
  CONSTRAINT `expenses_ibfk_2` FOREIGN KEY (`paid_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `expenses_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE `group_members` (
  `user_id` binary(16) NOT NULL,
  `group_id` binary(16) NOT NULL,
  `joined_at` timestamp NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `group_members_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `group_members_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE `settlement` (
  `settlement_id` binary(16) DEFAULT NULL,
  `amount` decimal(12,2) NOT NULL,
  `time` timestamp NOT NULL,
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `paid_to` binary(16) NOT NULL,
  `paid_by` binary(16) NOT NULL,
  `group_id` binary(16) NOT NULL,
  KEY `paid_to` (`paid_to`),
  KEY `paid_by` (`paid_by`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `settlement_ibfk_1` FOREIGN KEY (`paid_to`) REFERENCES `users` (`user_id`),
  CONSTRAINT `settlement_ibfk_2` FOREIGN KEY (`paid_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `settlement_ibfk_3` FOREIGN KEY (`group_id`) REFERENCES `groups` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE `expense_splits` (
  `user_id` binary(16) NOT NULL,
  `expense_id` binary(16) NOT NULL,
  `share_amount` decimal(12,2) NOT NULL,
  PRIMARY KEY (`user_id`,`expense_id`),
  KEY `expense_id` (`expense_id`),
  CONSTRAINT `expense_splits_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `expense_splits_ibfk_2` FOREIGN KEY (`expense_id`) REFERENCES `expenses` (`expense_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci