DELETE FROM STUDENTS;

INSERT INTO students (id, student_name, student_email)
VALUES ('2d629e0e-2c1d-458b-b936-4ba68ff3d59c', 'Oli', 'oli@gmail.com'),
       (RANDOM_UUID(), 'Patrik', 'patrik@gmail.com'),
       (RANDOM_UUID(), 'Juli', 'juli@gmail.com');