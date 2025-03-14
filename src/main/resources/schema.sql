CREATE TABLE users (
                       user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(100) NOT NULL,
                       password_hash TEXT NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       phone_number VARCHAR(20),
                       address TEXT,
                       role VARCHAR(50) NOT NULL,
                       nic VARCHAR(20) UNIQUE
);
