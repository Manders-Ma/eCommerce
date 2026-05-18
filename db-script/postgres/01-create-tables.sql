-- PostgreSQL schema for ecommerce
-- This script creates tables converted from MySQL to PostgreSQL
-- NOTE: run as a superuser for CREATE DATABASE / use psql to connect to the database

-- Optional: create database (run outside of a transaction in psql)
-- DROP DATABASE IF EXISTS ecommerce;
-- CREATE DATABASE ecommerce;
-- \c ecommerce

-- Safety: drop tables if exists (in correct order)
DROP TABLE IF EXISTS order_item CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS shipping_address CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS product_category CASCADE;

-- -----------------------------------------------------
-- Table: shipping_address
-- -----------------------------------------------------
CREATE TABLE shipping_address (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) UNIQUE NOT NULL,
  address VARCHAR(255) UNIQUE NOT NULL
);

-- -----------------------------------------------------
-- Table: member
-- -----------------------------------------------------
CREATE TABLE member (
  member_id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL,
  date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------
-- Table: customer
-- -----------------------------------------------------
CREATE TABLE customer (
  id BIGSERIAL PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  member_id BIGINT NOT NULL,
  CONSTRAINT fk_customer_member FOREIGN KEY(member_id) REFERENCES member(member_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT uq_customer_unique UNIQUE(first_name, last_name, email, member_id)
);

-- -----------------------------------------------------
-- Table: product_category
-- -----------------------------------------------------
CREATE TABLE product_category (
  id BIGSERIAL PRIMARY KEY,
  category_name VARCHAR(255)
);

-- -----------------------------------------------------
-- Table: product
-- -----------------------------------------------------
CREATE TABLE product (
  id BIGSERIAL PRIMARY KEY,
  sku VARCHAR(255),
  name VARCHAR(255),
  description VARCHAR(255),
  unit_price INTEGER,
  image_url VARCHAR(255),
  active BOOLEAN DEFAULT TRUE,
  units_in_stock INTEGER,
  date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  category_id BIGINT NOT NULL,
  CONSTRAINT fk_product_category FOREIGN KEY(category_id) REFERENCES product_category(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- index for product category lookups
CREATE INDEX idx_product_category_id ON product(category_id);

-- trigger helper to update last_updated on update
CREATE OR REPLACE FUNCTION update_last_updated_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.last_updated = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- attach triggers to tables that used ON UPDATE CURRENT_TIMESTAMP in MySQL
CREATE TRIGGER trg_product_last_updated
  BEFORE UPDATE ON product
  FOR EACH ROW
  EXECUTE FUNCTION update_last_updated_column();

-- -----------------------------------------------------
-- Table: orders
-- -----------------------------------------------------
CREATE TABLE orders (
  id BIGSERIAL PRIMARY KEY,
  order_tracking_number VARCHAR(255),
  total_price INTEGER,
  total_quantity INTEGER,
  customer_id BIGINT,
  shipping_address_id BIGINT,
  status VARCHAR(128),
  date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_orders_customer FOREIGN KEY(customer_id) REFERENCES customer(id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_orders_shipping_address FOREIGN KEY(shipping_address_id) REFERENCES shipping_address(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_shipping_address_id ON orders(shipping_address_id);

CREATE TRIGGER trg_orders_last_updated
  BEFORE UPDATE ON orders
  FOR EACH ROW
  EXECUTE FUNCTION update_last_updated_column();

-- -----------------------------------------------------
-- Table: order_item
-- -----------------------------------------------------
CREATE TABLE order_item (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255),
  image_url VARCHAR(255),
  quantity INTEGER,
  unit_price INTEGER,
  order_id BIGINT,
  product_id BIGINT,
  CONSTRAINT fk_order_item_order FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_order_item_order_id ON order_item(order_id);

-- -----------------------------------------------------
-- Notes:
-- - MySQL AUTO_INCREMENT replaced by BIGSERIAL/BIGINT + sequence
-- - Unsigned types removed (Postgres does not have unsigned integers)
-- - DATETIME -> TIMESTAMP
-- - ENGINE/CHARSET removed
-- - ON UPDATE CURRENT_TIMESTAMP implemented with trigger function above
-- - If you prefer IDENTITY instead of SERIAL/BIGSERIAL, replace "BIGSERIAL" with
--   "BIGINT GENERATED BY DEFAULT AS IDENTITY" for stricter SQL standards.

