CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    vin TEXT NOT NULL,
    make TEXT NOT NULL,
    model TEXT NOT NULL,
    year INT NOT NULL,
    group_name TEXT NOT NULL,
    average_speed DOUBLE PRECISION NOT NULL,
    device_id INT NOT NULL,
    last_odometer_reading DOUBLE PRECISION
);