-- Fix item_unit column type mismatch in PostgreSQL
-- Run this script in your PostgreSQL database management tool (pgAdmin, DBeaver, etc.)

-- First, check current column type
SELECT column_name, data_type, is_nullable
FROM information_schema.columns 
WHERE table_name = 'product_new' AND column_name = 'item_unit';

-- Convert JSON column to TEXT to match the Java entity expectations
-- This handles the type casting issue
ALTER TABLE product_new ALTER COLUMN item_unit TYPE TEXT USING item_unit::TEXT;

-- Verify the change
SELECT column_name, data_type, is_nullable
FROM information_schema.columns 
WHERE table_name = 'product_new' AND column_name = 'item_unit';

-- Optional: Add a comment to document the column purpose
COMMENT ON COLUMN product_new.item_unit IS 'JSON data stored as TEXT - contains product unit details and variants';