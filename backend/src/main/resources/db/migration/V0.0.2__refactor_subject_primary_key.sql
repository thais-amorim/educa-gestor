-- Drop foreign key constraints that reference subject.id
ALTER TABLE semester_subject DROP FOREIGN KEY semester_subject_ibfk_1;

-- Drop the primary key constraint on subject.id
ALTER TABLE subject DROP PRIMARY KEY;

-- Drop the id column from subject table
ALTER TABLE subject DROP COLUMN id;

-- Make code the primary key
ALTER TABLE subject ADD PRIMARY KEY (code);

-- Update semester_subject table to reference subject.code instead of subject.id
ALTER TABLE semester_subject DROP COLUMN subject_id;
ALTER TABLE semester_subject ADD COLUMN subject_code VARCHAR(50) NOT NULL;

-- Add the foreign key constraint back, now referencing subject.code
ALTER TABLE semester_subject ADD FOREIGN KEY (subject_code) REFERENCES subject(code);
