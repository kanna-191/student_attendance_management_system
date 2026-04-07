CREATE TABLE students (
    roll_no VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    department VARCHAR(50),
    section VARCHAR(10),
    mobile VARCHAR(15),
    present_days INT DEFAULT 0,
    total_days INT DEFAULT 0
);