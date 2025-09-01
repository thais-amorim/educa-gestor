-- Remove begin_at and end_at columns from subject table
ALTER TABLE subject DROP COLUMN begin_at;
ALTER TABLE subject DROP COLUMN end_at;
