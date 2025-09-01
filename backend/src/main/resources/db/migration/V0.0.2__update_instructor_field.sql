-- Remove the instructor_id column and add instructor_name column
ALTER TABLE subject DROP COLUMN instructor_id;
ALTER TABLE subject ADD COLUMN instructor_name VARCHAR(200) NULL;
