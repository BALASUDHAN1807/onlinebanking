CREATE DATABASE IF NOT EXISTS kdbank;
USE kdbank;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE,
  full_name VARCHAR(100),
  phone VARCHAR(20) UNIQUE,
  email VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  account_number VARCHAR(20) UNIQUE,
  type VARCHAR(20),
  balance DECIMAL(15,2),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE transactions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  account_id INT,
  type VARCHAR(20),
  amount DECIMAL(15,2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

-- sample user
INSERT INTO users(username, full_name, phone, email) VALUES('john123','John Doe','9876543210','john@example.com');
INSERT INTO accounts(user_id, account_number, type, balance) VALUES(1,'SAV100001','SAVINGS',5000.00);
INSERT INTO accounts(user_id, account_number, type, balance) VALUES(1,'CUR100001','CURRENT',2000.00);
INSERT INTO transactions(account_id, type, amount) VALUES(1,'CREDIT',1000.00);
INSERT INTO transactions(account_id, type, amount) VALUES(1,'DEBIT',200.00);
