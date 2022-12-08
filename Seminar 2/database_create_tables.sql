CREATE TYPE skill AS enum ('Beginner', 'Intermediate', 'Advanced');

--Classes
CREATE TABLE lesson
  (
     database_id SERIAL UNIQUE NOT NULL,
     lesson_id   VARCHAR(100) UNIQUE NOT NULL,
     skill_level SKILL NOT NULL,
     PRIMARY KEY (database_id)
  );

CREATE TABLE person
  (
     database_id     SERIAL UNIQUE NOT NULL,
     personal_number VARCHAR(100) UNIQUE NOT NULL,
     full_name       VARCHAR(500) NOT NULL,
     email           VARCHAR(500) NOT NULL,
     PRIMARY KEY (database_id)
  );

--Inherits
CREATE TABLE enrollment_applications
  (
     application_id   VARCHAR(100) UNIQUE NOT NULL,
     cover_letter     TEXT,
     application_date DATE
  )inherits(person);

ALTER TABLE enrollment_applications
  ADD CONSTRAINT db_unique_ea UNIQUE (database_id);

ALTER TABLE enrollment_applications
  ADD PRIMARY KEY (database_id);

CREATE TABLE instructors
  (
     employee_id VARCHAR(100) NOT NULL
  )inherits(person);

ALTER TABLE instructors
  ADD CONSTRAINT db_unique_is UNIQUE (database_id);

ALTER TABLE instructors
  ADD PRIMARY KEY (database_id);

CREATE TABLE administrators
  (
     employee_id VARCHAR(100) NOT NULL,
     salary      INT
  )inherits(person);

ALTER TABLE administrators
  ADD CONSTRAINT db_unique_a UNIQUE (database_id);

ALTER TABLE administrators
  ADD PRIMARY KEY (database_id);

CREATE TABLE ensemble
  (
     genre                  VARCHAR(100) NOT NULL,
     min_number_of_students INT NOT NULL,
     max_number_of_students INT NOT NULL,
     instructor_db_id       INT NOT NULL,
     CONSTRAINT fk_instructor FOREIGN KEY (instructor_db_id) REFERENCES
     instructors(database_id)
  )inherits(lesson);

ALTER TABLE ensemble
  ADD CONSTRAINT db_unique_ee UNIQUE (database_id);

ALTER TABLE ensemble
  ADD PRIMARY KEY (database_id);

CREATE TABLE group_lesson
  (
     instrument             VARCHAR(100) NOT NULL,
     min_number_of_students INT NOT NULL,
     max_number_of_students INT NOT NULL,
     instructor_db_id       INT NOT NULL,
     CONSTRAINT fk_instructor FOREIGN KEY (instructor_db_id) REFERENCES
     instructors(database_id)
  )inherits(lesson);

ALTER TABLE group_lesson
  ADD CONSTRAINT db_unique_gl UNIQUE (database_id);

ALTER TABLE group_lesson
  ADD PRIMARY KEY (database_id);

--Inherits and foreign key
CREATE TABLE private_lesson
  (
     instrument       VARCHAR(100) NOT NULL,
     instructor_db_id INT NOT NULL,
     CONSTRAINT fk_instructor FOREIGN KEY (instructor_db_id) REFERENCES
     instructors(database_id)
  )inherits(lesson);

ALTER TABLE private_lesson
  ADD CONSTRAINT db_unique_pl UNIQUE (database_id);

ALTER TABLE private_lesson
  ADD PRIMARY KEY (database_id);

CREATE TABLE student
  (
     student_id VARCHAR(100) NOT NULL
  )inherits(person);

ALTER TABLE student
  ADD CONSTRAINT db_unique_st UNIQUE (database_id);

ALTER TABLE student
  ADD PRIMARY KEY (database_id);

CREATE TABLE physical_instruments
  (
     database_id   SERIAL UNIQUE NOT NULL,
     instrument_id VARCHAR(100) UNIQUE NOT NULL,
     brand         VARCHAR(100) NOT NULL,
     price         INT NOT NULL,
     instrument_type VARCHAR(100) NOT NULL,
     PRIMARY KEY (database_id)
  );

CREATE TABLE address
  (
     database_id         SERIAL UNIQUE NOT NULL,
     street_address      VARCHAR(500) NOT NULL,
     zip_code            VARCHAR(10) NOT NULL,
     city                VARCHAR(100) NOT NULL,
          PRIMARY KEY (database_id),
          student_db_id       INT,
          instructor_db_id    INT,
          administrator_db_id INT,
     --Student
     CONSTRAINT fk_student FOREIGN KEY (student_db_id) REFERENCES student(
          database_id),
     --Instructors
     CONSTRAINT fk_instructor FOREIGN KEY (instructor_db_id) REFERENCES
          instructors(database_id),
     --Administrators
     CONSTRAINT fk_administrators FOREIGN KEY (administrator_db_id) REFERENCES
          administrators(database_id)
  );

--Has foreign keys
CREATE TABLE phone
  (
     phone_number        VARCHAR(500) UNIQUE NOT NULL,
          PRIMARY KEY (phone_number),
          student_db_id       INT,
          instructor_db_id    INT,
          administrator_db_id INT,
     --Student
     CONSTRAINT fk_student FOREIGN KEY (student_db_id) REFERENCES student(
          database_id),
     --Instructors
     CONSTRAINT fk_instructor FOREIGN KEY (instructor_db_id) REFERENCES
          instructors(database_id),
     --Administrators
     CONSTRAINT fk_administrators FOREIGN KEY (administrator_db_id) REFERENCES
          administrators(database_id)
  );

CREATE TABLE contact_person
  (
     database_id         SERIAL UNIQUE NOT NULL,
     full_name           VARCHAR(500) NOT NULL,
     phone_number        VARCHAR(500) NOT NULL,
     email               VARCHAR(500),
          PRIMARY KEY (database_id),
          student_db_id       INT,
          instructor_db_id    INT,
          administrator_db_id INT,
     --Student
     CONSTRAINT fk_student FOREIGN KEY (student_db_id) REFERENCES student(
          database_id),
     --Instructors
     CONSTRAINT fk_instructor FOREIGN KEY (instructor_db_id) REFERENCES
          instructors(database_id),
     --Administrators
     CONSTRAINT fk_administrators FOREIGN KEY (administrator_db_id) REFERENCES
          administrators(database_id)
  );

CREATE TABLE booking_table
  (
     database_id          SERIAL UNIQUE NOT NULL,
     "date"               DATE NOT NULL,
     "time"               TIME NOT NULL,
     classroom            VARCHAR(100) NOT NULL,
          PRIMARY KEY (database_id),
          private_lesson_db_id INT,
          ensemble_db_id       INT,
          group_lesson_db_id   INT,
     CONSTRAINT fk_private FOREIGN KEY (private_lesson_db_id) REFERENCES
          private_lesson(database_id),
     CONSTRAINT fk_ensemble FOREIGN KEY (ensemble_db_id) REFERENCES ensemble(
          database_id),
     CONSTRAINT fk_group FOREIGN KEY (group_lesson_db_id) REFERENCES
          group_lesson(database_id)
  );

CREATE TABLE rented_instrument
  (
     receipt_id       VARCHAR(100) UNIQUE NOT NULL,
     start_date       TIMESTAMP NOT NULL,
     end_date         TIMESTAMP,
     student_db_id    INT NOT NULL,
     instrument_db_id INT NOT NULL,
     PRIMARY KEY (student_db_id, instrument_db_id),
     CONSTRAINT fk_instrument FOREIGN KEY (instrument_db_id) REFERENCES
     physical_instruments(database_id),
     CONSTRAINT fk_person FOREIGN KEY (student_db_id) REFERENCES student(
     database_id)
  );

CREATE TABLE proficiencies
  (
     instrument       VARCHAR(100) NOT NULL,
     instructor_db_id INT NOT NULL,
     skill_level      SKILL NOT NULL,
     PRIMARY KEY (instrument, instructor_db_id),
     CONSTRAINT fk_instructors FOREIGN KEY (instructor_db_id) REFERENCES
     instructors(database_id)
  );

CREATE TABLE bridge_sibling
  (
     sibling_id_1 INT NOT NULL,
     sibling_id_2 INT NOT NULL,
     CONSTRAINT sibling1 FOREIGN KEY (sibling_id_1) REFERENCES student(
     database_id),
     CONSTRAINT sibling2 FOREIGN KEY (sibling_id_2) REFERENCES student(
     database_id),
     PRIMARY KEY(sibling_id_1, sibling_id_2)
  );

CREATE TABLE bridge_student_to_booking
  (
     student_db_id INT NOT NULL,
     booking_db_id INT NOT NULL,
     CONSTRAINT student FOREIGN KEY (student_db_id) REFERENCES student(
     database_id),
     CONSTRAINT booking FOREIGN KEY (booking_db_id) REFERENCES booking_table(
     database_id),
     PRIMARY KEY (student_db_id, booking_db_id)
  );

CREATE TABLE price_table
  (
     private_lesson_base_price  INT,
     group_lesson_base_price    INT,
     ensemble_lesson_base_price INT,
     sibling_discount_modifier  REAL,
     start_date                 DATE NOT NULL,
     end_date                   DATE
  ); 
