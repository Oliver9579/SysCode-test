CREATE TABLE IF NOT EXISTS students
(
    id            UUID         NOT NULL PRIMARY KEY,
    student_name  VARCHAR(50)  NOT NULL,
    student_email VARCHAR(255) NOT NULL
    );

INSERT
INTO students (id, student_name, student_email)
VALUES (random_uuid(), 'Oli', 'oli@gmail.com'),
       (random_uuid(), 'Patrik', 'patrik@gmail.com'),
       (random_uuid(), 'Juli', 'juli@gmail.com');