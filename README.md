CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contact_number VARCHAR(20),
    user_type VARCHAR(30) NOT NULL
);

CREATE TABLE Locations (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    location_name VARCHAR(100) NOT NULL,
    building VARCHAR(100),
    description VARCHAR(255)
);

CREATE TABLE Items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    location_id INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    description TEXT,
    date_reported DATE NOT NULL,
    item_type VARCHAR(10) NOT NULL,
    item_status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (location_id) REFERENCES Locations(location_id)
);

CREATE TABLE Claims (
    claim_id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL,
    user_id INT NOT NULL,
    claim_date DATE NOT NULL,
    proof_description TEXT,
    claim_status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (item_id) REFERENCES Items(item_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
