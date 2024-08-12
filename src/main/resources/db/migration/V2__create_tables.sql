CREATE TABLE entities.users (
                                id SERIAL PRIMARY KEY,
                                name VARCHAR(255),
                                password VARCHAR(255),
                                role VARCHAR(50)
);

CREATE TABLE entities.audit_logs (
                                     id SERIAL PRIMARY KEY,
                                     action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     user_id INT,
                                     action_type VARCHAR(255),
                                     description TEXT,
                                     FOREIGN KEY (user_id) REFERENCES entities.users(id)
);

CREATE TABLE entities.cars (
                               id SERIAL PRIMARY KEY,
                               brand VARCHAR(255),
                               model VARCHAR(255),
                               year INT,
                               price DECIMAL(10, 2),
                               condition VARCHAR(50),
                               status VARCHAR(50)
);

CREATE TABLE entities.clients (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR(255),
                                  contact_info VARCHAR(255)
);

CREATE TABLE entities.employees (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(255),
                                    contact_info VARCHAR(255),
                                    role VARCHAR(50)
);

CREATE TABLE entities.orders (
                                 id SERIAL PRIMARY KEY,
                                 client_id INT,
                                 car_id INT,
                                 order_date DATE,
                                 status VARCHAR(50),
                                 FOREIGN KEY (client_id) REFERENCES entities.clients(id),
                                 FOREIGN KEY (car_id) REFERENCES entities.cars(id)
);

CREATE TABLE entities.service_requests (
                                           id SERIAL PRIMARY KEY,
                                           description TEXT,
                                           status VARCHAR(50)
);
