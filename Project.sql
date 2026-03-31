CREATE DATABASE phone_store_db;
USE phone_store_db;

CREATE TABLE users (
                       id int auto_increment primary key,
                       name varchar(100) not null,
                       email varchar(100) unique not null,
                       password varchar(255) not null,
                       phone varchar(20),
                       address varchar(255),
                       role enum('ADMIN', 'CUSTOMER') default 'CUSTOMER',
                       created_at timestamp default current_timestamp
);

CREATE TABLE categories (
                            id int auto_increment primary key,
                            name varchar(100) not null,	
                            description text
);

CREATE TABLE products (
                          id int auto_increment primary key,
                          name varchar(150) not null,
                          brand varchar(100) not null,
                          storage varchar(50),
                          color varchar(50),
                          price decimal(12,2) not null,
                          stock int default 0,
                          description text,
                          category_id int,
                          created_at timestamp default current_timestamp,

                          foreign key (category_id) references categories(id) on delete set null
);

CREATE TABLE cart (
                      id int auto_increment primary key,
                      user_id int,
                      product_id int,
                      quantity int default 1,

                      foreign key (user_id) references users(id) on delete cascade,
                      foreign key (product_id) references products(id) on delete cascade
);

CREATE TABLE orders (
                        id int auto_increment primary key,
                        user_id int,
                        total_price decimal(12,2),
                        status enum('PENDING', 'SHIPPING', 'DELIVERED', 'CANCELLED') default 'PENDING',
                        shipping_address varchar(255),
                        created_at timestamp default current_timestamp,

                        foreign key (user_id) references users(id)
);

CREATE TABLE order_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT,
    price DECIMAL(12,2),

    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

INSERT INTO users(name, email, password, role)
VALUES 
('Admin', 'admin@gmail.com', 'admin', 'ADMIN'),
('Nhật Hoàng', 'nghoangnhat20@gmail.com', '123456', 'CUSTOMER');

INSERT INTO categories(name, description)
VALUES 
('Phone', 'Điện thoại'),
('Laptop', 'Máy tính xách tay'),
('Accessory', 'Phụ kiện');

INSERT INTO products(name, brand, storage, color, price, stock, category_id)
VALUES 
('iPhone 15', 'Apple', '128GB', 'Black', 2000, 10, 1),
('iPhone 15 Pro', 'Apple', '256GB', 'Silver', 3000, 5, 1),
('Samsung S24', 'Samsung', '256GB', 'Blue', 1800, 8, 1),
('MacBook Air M2', 'Apple', '512GB', 'Gray', 2500, 3, 2);

INSERT INTO cart(user_id, product_id, quantity)
VALUES 
(2, 1, 2),
(2, 2, 1);

INSERT INTO orders(user_id, total_price, status, shipping_address)
VALUES 
(2, 7000, 'PENDING', 'Hà Nội');

INSERT INTO order_details(order_id, product_id, quantity, price)
VALUES 
(1, 1, 2, 2000),
(1, 2, 1, 3000);

UPDATE users 
SET password = '123456'											
WHERE email = 'admin@gmail.com';

ALTER TABLE users 
ADD status ENUM('ACTIVE', 'BLOCKED') DEFAULT 'ACTIVE';

