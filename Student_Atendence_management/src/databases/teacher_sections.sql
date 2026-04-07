CREATE TABLE teacher_sections (
    teacher_id INT,
    department VARCHAR(50),
    section VARCHAR(10),

    CONSTRAINT teacher_sections_ibfk_1
    FOREIGN KEY (teacher_id)
    REFERENCES teachers(id)
    ON DELETE CASCADE
);