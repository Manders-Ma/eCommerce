-- -----------------------------------------------------
-- Database ecommerce
-- -----------------------------------------------------

USE `ecommerce`;

--
-- Prep work
--
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `customer`;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS `shipping_address`;
SET FOREIGN_KEY_CHECKS=1;

-- -----------------------------------------------------
-- Table structure for table `shipping_address`
-- -----------------------------------------------------
CREATE TABLE `shipping_address` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) UNIQUE NOT NULL,
  `address` varchar(255) UNIQUE NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Table structure for table `member` 
-- -----------------------------------------------------
CREATE TABLE `member` (
	`member_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT, 
    `name` varchar(255) NOT NULL, 
    `email` varchar(255) NOT NULL, 
    `password` varchar(255) NOT NULL, 
    `role` varchar(255) NOT NULL, 
    `date_created` DATETIME DEFAULT CURRENT_TIMESTAMP, 
    PRIMARY KEY (`member_id`), 
    UNIQUE KEY (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Table structure for table `customer`
-- -----------------------------------------------------
CREATE TABLE `customer` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `date_created` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `member_id` BIGINT UNSIGNED NOT NULL, 
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
  ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE KEY (`first_name`, `last_name`, `email`, `member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Table structure for table `orders`
-- -----------------------------------------------------
CREATE TABLE `orders` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_tracking_number` varchar(255) DEFAULT NULL,
  `total_price` INT(11) UNSIGNED DEFAULT NULL,
  `total_quantity` INT(11) UNSIGNED DEFAULT NULL,
  `customer_id` bigint UNSIGNED DEFAULT NULL,
  `shipping_address_id` bigint UNSIGNED DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  `date_created` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_updated` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `K_customer_id` (`customer_id`),
  KEY `K_shipping_address_id` (`shipping_address_id`),
  CONSTRAINT `FK_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
  ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_shipping_address_id` FOREIGN KEY (`shipping_address_id`) REFERENCES `shipping_address` (`id`)
  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Table structure for table `order_items`
-- -----------------------------------------------------
CREATE TABLE `order_item` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `quantity` INT(11) UNSIGNED DEFAULT NULL,
  `unit_price` INT(11) UNSIGNED DEFAULT NULL,
  `order_id` bigint UNSIGNED DEFAULT NULL,
  `product_id` bigint UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `K_order_id` (`order_id`),
  CONSTRAINT `FK_orders_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
  ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Add sample data [shipping address]
-- -----------------------------------------------------
INSERT INTO `shipping_address`(`name`, `address`) VALUES('測試一店', '測試一店路66號');
INSERT INTO `shipping_address`(`name`, `address`) VALUES('測試二店', '測試二店路77號');
INSERT INTO `shipping_address`(`name`, `address`) VALUES('測試三店', '測試三店路88號');