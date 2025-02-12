CREATE TABLE IF NOT EXISTS users
(
    user_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash TEXT         NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    phone_number  VARCHAR(20) UNIQUE,
    address       TEXT,
    role          VARCHAR(20)  NOT NULL,
    nic           VARCHAR(20) UNIQUE,
    front_image   TEXT,
    back_image    TEXT,
    created_at    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_phone_number ON users (phone_number);


CREATE TABLE IF NOT EXISTS vehicles
(
    vehicle_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vehicle_number VARCHAR(20) NOT NULL UNIQUE,
    vehicle_type   VARCHAR(50) NOT NULL,
    vehicle_color  VARCHAR(30),
    vehicle_model  VARCHAR(50),
    vehicle_image  TEXT,
    status         VARCHAR(20) CHECK (status IN ('AVAILABLE', 'UNAVAILABLE')) DEFAULT 'AVAILABLE',
    driver_id      UUID  DEFAULT gen_random_uuid(),
    driver_name    VARCHAR(100),
    driver_contact VARCHAR(20),
    driver_nic     VARCHAR(20) UNIQUE,
    driver_email   VARCHAR(100) UNIQUE
);

CREATE INDEX IF NOT EXISTS idx_vehicle_number ON vehicles (vehicle_number);
CREATE INDEX IF NOT EXISTS idx_driver ON vehicles (driver_id);

CREATE TABLE IF NOT EXISTS trips
(
    trip_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id     UUID NOT NULL,
    vehicle_id      UUID NOT NULL,
    date            DATE NOT NULL,
    start_longitude DECIMAL(10, 7),
    start_latitude  DECIMAL(10, 7),
    end_longitude   DECIMAL(10, 7),
    end_latitude    DECIMAL(10, 7),
    fare            INT  NOT NULL,
    destination     TEXT,
    distance        VARCHAR(20),
    startTime       TIMESTAMP,
    endTime         TIMESTAMP,
    created_at      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (vehicle_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_customer ON trips (customer_id);
CREATE INDEX IF NOT EXISTS idx_vehicle ON trips (vehicle_id);
CREATE INDEX IF NOT EXISTS idx_date ON trips (date);

