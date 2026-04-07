CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(20),
    date DATE,
    status VARCHAR(10),

    FOREIGN KEY (roll_no)
    REFERENCES students(roll_no)
    ON DELETE CASCADE
);