CREATE TYPE skill AS ENUM ('Beginner', 'Intermediate', 'Advanced');
--Classes
CREATE TABLE lesson (
  database_id SERIAL UNIQUE NOT NULL,
  lesson_id varchar(100) UNIQUE NOT NULL,
  skill_level varchar(100) NOT NULL,
  PRIMARY KEY (database_id)
);

CREATE TABLE person (
  database_id SERIAL UNIQUE NOT NULL,
  personal_number varchar(100) UNIQUE NOT NULL,
  full_name varchar(500) NOT NULL,
  email varchar(500) NOT NULL,
  PRIMARY KEY (database_id)
);

--Inherits
CREATE TABLE enrollment_applications (
  application_id varchar(100) UNIQUE NOT NULL,
  cover_letter text,
  application_date date
)inherits(person);
ALTER TABLE enrollment_applications
ADD constraint db_unique_ea UNIQUE (database_id); 
ALTER TABLE enrollment_applications
ADD PRIMARY KEY (database_id); 

CREATE TABLE instructors (
  employee_id varchar(100) NOT NULL
)inherits(person);
ALTER TABLE instructors
ADD constraint db_unique_is UNIQUE (database_id); 
ALTER TABLE instructors
ADD PRIMARY KEY (database_id); 

CREATE TABLE administrators (
  employee_id varchar(100)  NOT NULL,
  salary int
)inherits(person);
ALTER TABLE administrators
ADD constraint db_unique_a UNIQUE (database_id); 
ALTER TABLE administrators
ADD PRIMARY KEY (database_id); 

CREATE TABLE ensemble (
  genre varchar(100)  NOT NULL,
  min_number_of_students int NOT NULL,
  max_number_of_students int NOT NULL,
  instructor_id int NOT NULL,
  constraint fk_instructor
	foreign key (instructor_id)
	references instructors(database_id)
)inherits(lesson);
ALTER TABLE ensemble
ADD constraint db_unique_ee UNIQUE (database_id); 
ALTER TABLE ensemble
ADD PRIMARY KEY (database_id); 

CREATE TABLE group_lesson (
  instrument varchar(100) NOT NULL,
  min_number_of_students int NOT NULL,
  max_number_of_students int NOT NULL,
	instructor_id int NOT NULL,
	constraint fk_instructor
	foreign key (instructor_id)
	references instructors(database_id)
)inherits(lesson);
ALTER TABLE group_lesson
ADD constraint db_unique_gl UNIQUE (database_id); 
ALTER TABLE group_lesson
ADD PRIMARY KEY (database_id); 

--Inherits and foreign key

CREATE TABLE private_lesson (
  instrument varchar(100) NOT NULL,
	instructor_id int NOT NULL,
	constraint fk_instructor
	foreign key (instructor_id)
	references instructors(database_id)
)inherits(lesson);
ALTER TABLE private_lesson
ADD constraint db_unique_pl UNIQUE (database_id); 
ALTER TABLE private_lesson
ADD PRIMARY KEY (database_id); 

CREATE TABLE student (
  student_id varchar(100) NOT NULL,
  private_lesson_id int,
	sibling_id int,
	constraint fk_lesson
	foreign key (private_lesson_id)
	references private_lesson(database_id)
)inherits(person);
ALTER TABLE student
ADD constraint db_unique_st UNIQUE (database_id); 
ALTER TABLE student
ADD PRIMARY KEY (database_id); 
ALTER TABLE student
ADD CONSTRAINT fk_sibling 
FOREIGN KEY (sibling_id) 
REFERENCES student(database_id);

CREATE TABLE physical_instruments (
  database_id SERIAL UNIQUE NOT NULL,
  instrument_id varchar(100) UNIQUE NOT NULL,
  brand varchar(100) NOT NULL,
  price int NOT NULL,
  PRIMARY KEY (database_id)
);

CREATE TABLE address (
  database_id SERIAL UNIQUE NOT NULL,
  street_address varchar(500) NOT NULL,
  zip_code varchar(10) NOT NULL,
  city varchar(100) NOT NULL,
  PRIMARY KEY (database_id),
	student_id int,
	instructor_id int,
	administrator_id int,
	--Student
	constraint fk_student
	foreign key (student_id)
	references student(database_id),
	--Instructors
	constraint fk_instructor
	foreign key (instructor_id)
	references instructors(database_id),
	--Administrators
	constraint fk_administrators
	foreign key (administrator_id)
	references administrators(database_id)
);

--Has foreign keys
CREATE TABLE phone (
  phone_number varchar(500) UNIQUE NOT NULL,
  PRIMARY KEY (phone_number),
	student_id int,
	instructor_id int,
	administrator_id int,
	--Student
	constraint fk_student
	foreign key (student_id)
	references student(database_id),
	--Instructors
	constraint fk_instructor
	foreign key (instructor_id)
	references instructors(database_id),
	--Administrators
	constraint fk_administrators
	foreign key (administrator_id)
	references administrators(database_id)
);

CREATE TABLE contact_person (
  contact_id SERIAL UNIQUE NOT NULL,
  full_name varchar(500) NOT NULL,
  phone_number varchar(500) NOT NULL,
  email varchar(500),
	PRIMARY KEY (contact_id),
	student_id int,
	instructor_id int,
	administrator_id int,
	--Student
	constraint fk_student
	foreign key (student_id)
	references student(database_id),
	--Instructors
	constraint fk_instructor
	foreign key (instructor_id)
	references instructors(database_id),
	--Administrators
	constraint fk_administrators
	foreign key (administrator_id)
	references administrators(database_id)
);

CREATE TABLE time_slot (
	database_id SERIAL UNIQUE NOT NULL,
  "date" date NOT NULL,
  "time" time NOT NULL,
  classroom varchar(100) NOT NULL,
  PRIMARY KEY (database_id),
	private_lesson_id int,
	ensemble_id int,
	group_lesson_id int,
	CONSTRAINT fk_private
	foreign key (private_lesson_id)
	references private_lesson(database_id),
	CONSTRAINT fk_ensemble
	foreign key (ensemble_id)
	references ensemble(database_id),
	CONSTRAINT fk_group
	foreign key (group_lesson_id)
	references group_lesson(database_id)
);

CREATE TABLE rented_instrument (
  receipt_id varchar(100) UNIQUE NOT NULL,
  start_date timestamp NOT NULL,
  end_date timestamp,
  student_id int NOT NULL,
  instrument_id int NOT NULL,
  PRIMARY KEY (student_id, instrument_id),
  CONSTRAINT fK_instrument
    FOREIGN KEY (instrument_id)
      REFERENCES physical_instruments(database_id),
	constraint fk_person
	foreign key (student_id)
	references student(database_id)
);

CREATE TABLE proficiencies (
  instrument varchar(100) NOT NULL,
  instructor_id int NOT NULL,
  skill_Level skill NOT NULL,
  PRIMARY KEY (instrument, instructor_id),
	constraint fk_instructors
	foreign key (instructor_id)
	references instructors(database_id)
);

CREATE TABLE bridge (
  database_id SERIAL UNIQUE NOT NULL,
  student_id int,
  ensemble_id int,
  group_lesson_id int,
  PRIMARY KEY (database_id),
  CONSTRAINT fk_student
    FOREIGN KEY (student_id)
      REFERENCES student(database_id),
  CONSTRAINT fk_ensemble
    FOREIGN KEY (ensemble_id)
      REFERENCES ensemble(database_id),
  CONSTRAINT fk_group_lesson
    FOREIGN KEY (group_lesson_id)
      REFERENCES group_lesson(database_id)
);
