CREATE TABLE car_inventory (
    car_type VARCHAR(50) PRIMARY KEY,
    available_count INT,
	max_count integer
);

CREATE TABLE car_reservation (
    id SERIAL PRIMARY KEY,
    car_type VARCHAR(50),
    reservation_date TIMESTAMP,
    duration_days INT,
    FOREIGN KEY (car_type) REFERENCES car_inventory(car_type)
);


-- Insert initial data into the car_inventory table
INSERT INTO car_inventory (car_type, available_count) 
VALUES 
    ('Sedan', 10, 10),  
    ('SUV', 5, 5),     
    ('Van', 5, 5);     
