CREATE TABLE IF NOT EXISTS students
(
    id            UUID         NOT NULL PRIMARY KEY,
    student_name  VARCHAR(50)  NOT NULL,
    student_email VARCHAR(255) NOT NULL
);
