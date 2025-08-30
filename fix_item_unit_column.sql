-- Migration script to fix item_unit column type mismatch
-- Run this script to change the column from JSON/JSONB to TEXT

-- First, let's alter the column type from JSON/JSONB to TEXT
ALTER TABLE product_new ALTER COLUMN item_unit TYPE TEXT;

-- If the above fails due to type conversion issues, use this alternative approach:
-- ALTER TABLE product_new ALTER COLUMN item_unit TYPE TEXT USING item_unit::TEXT;