-- SQL Script for creating and populating the new category, subcategory, and product tables
-- This script is compatible with PostgreSQL

-- Drop tables if they exist (in correct order due to foreign key constraints)
DROP TABLE IF EXISTS product_new CASCADE;
DROP TABLE IF EXISTS subcategory CASCADE;
DROP TABLE IF EXISTS category_new CASCADE;

-- Create category_new table
CREATE TABLE category_new (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(500),
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create subcategory table
CREATE TABLE subcategory (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url VARCHAR(500),
    category_new_id VARCHAR(255) NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_new_id) REFERENCES category_new(id) ON DELETE CASCADE
);

-- Create product_new table
CREATE TABLE product_new (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(500),
    quantity INTEGER NOT NULL DEFAULT 0,
    brand VARCHAR(255),
    unit VARCHAR(50),
    pua DECIMAL(10,3) DEFAULT NULL, -- Packaged Unit A - Weight/Volume
    pub DECIMAL(10,3) DEFAULT NULL, -- Packaged Unit B - Weight/Volume
    puc DECIMAL(10,3) DEFAULT NULL, -- Packaged Unit C - Weight/Volume
    item_unit JSON DEFAULT NULL, -- JSON field to store structured unit data
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    subcategory_id VARCHAR(255) NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subcategory_id) REFERENCES subcategory(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_subcategory_category_new_id ON subcategory(category_new_id);
CREATE INDEX idx_product_new_subcategory_id ON product_new(subcategory_id);
CREATE INDEX idx_product_new_is_active ON product_new(is_active);
CREATE INDEX idx_product_new_brand ON product_new(brand);
CREATE INDEX idx_product_new_price ON product_new(price);

-- Insert sample data into category_new
INSERT INTO category_new (id, name, description, image_url) VALUES
('cat-1', 'Fruits & Vegetables', 'Fresh fruits and vegetables from local farms', 'https://example.com/fruits-vegetables.jpg'),
('cat-2', 'Dairy & Eggs', 'Fresh dairy products and farm eggs', 'https://example.com/dairy-eggs.jpg'),
('cat-3', 'Meat & Seafood', 'Premium quality meat and fresh seafood', 'https://example.com/meat-seafood.jpg'),
('cat-4', 'Bakery', 'Fresh baked goods and bread', 'https://example.com/bakery.jpg'),
('cat-5', 'Beverages', 'Refreshing drinks and beverages', 'https://example.com/beverages.jpg'),
('cat-6', 'Pantry Staples', 'Essential cooking ingredients and pantry items', 'https://example.com/pantry.jpg'),
('cat-7', 'Snacks & Confectionery', 'Delicious snacks and sweet treats', 'https://example.com/snacks.jpg'),
('cat-8', 'Health & Wellness', 'Organic and health-focused products', 'https://example.com/health.jpg');

-- Insert sample data into subcategory
INSERT INTO subcategory (id, name, description, image_url, category_new_id) VALUES
-- Fruits & Vegetables subcategories
('sub-1', 'Fresh Fruits', 'Seasonal fresh fruits', 'https://example.com/fresh-fruits.jpg', 'cat-1'),
('sub-2', 'Fresh Vegetables', 'Farm-fresh vegetables', 'https://example.com/fresh-vegetables.jpg', 'cat-1'),
('sub-3', 'Organic Produce', 'Certified organic fruits and vegetables', 'https://example.com/organic-produce.jpg', 'cat-1'),
('sub-4', 'Exotic Fruits', 'Imported and exotic fruits', 'https://example.com/exotic-fruits.jpg', 'cat-1'),

-- Dairy & Eggs subcategories
('sub-5', 'Milk & Cream', 'Fresh milk and dairy products', 'https://example.com/milk-cream.jpg', 'cat-2'),
('sub-6', 'Cheese', 'Variety of fresh and aged cheeses', 'https://example.com/cheese.jpg', 'cat-2'),
('sub-7', 'Yogurt', 'Fresh yogurt and dairy desserts', 'https://example.com/yogurt.jpg', 'cat-2'),
('sub-8', 'Eggs', 'Farm-fresh eggs', 'https://example.com/eggs.jpg', 'cat-2'),

-- Meat & Seafood subcategories
('sub-9', 'Chicken', 'Fresh chicken and poultry', 'https://example.com/chicken.jpg', 'cat-3'),
('sub-10', 'Beef', 'Premium beef cuts', 'https://example.com/beef.jpg', 'cat-3'),
('sub-11', 'Fish', 'Fresh fish and seafood', 'https://example.com/fish.jpg', 'cat-3'),
('sub-12', 'Pork', 'Fresh pork products', 'https://example.com/pork.jpg', 'cat-3'),

-- Bakery subcategories
('sub-13', 'Bread', 'Fresh baked bread', 'https://example.com/bread.jpg', 'cat-4'),
('sub-14', 'Pastries', 'Sweet and savory pastries', 'https://example.com/pastries.jpg', 'cat-4'),
('sub-15', 'Cakes', 'Fresh cakes and desserts', 'https://example.com/cakes.jpg', 'cat-4'),

-- Beverages subcategories
('sub-16', 'Juices', 'Fresh and packaged juices', 'https://example.com/juices.jpg', 'cat-5'),
('sub-17', 'Soft Drinks', 'Carbonated and non-carbonated drinks', 'https://example.com/soft-drinks.jpg', 'cat-5'),
('sub-18', 'Coffee & Tea', 'Premium coffee and tea products', 'https://example.com/coffee-tea.jpg', 'cat-5'),
('sub-19', 'Water', 'Bottled and mineral water', 'https://example.com/water.jpg', 'cat-5'),

-- Pantry Staples subcategories
('sub-20', 'Rice & Grains', 'Various types of rice and grains', 'https://example.com/rice-grains.jpg', 'cat-6'),
('sub-21', 'Pasta', 'Different varieties of pasta', 'https://example.com/pasta.jpg', 'cat-6'),
('sub-22', 'Cooking Oil', 'Cooking oils and vinegars', 'https://example.com/cooking-oil.jpg', 'cat-6'),
('sub-23', 'Spices & Seasonings', 'Herbs, spices, and seasonings', 'https://example.com/spices.jpg', 'cat-6'),

-- Snacks & Confectionery subcategories
('sub-24', 'Chips & Crisps', 'Various flavored chips and crisps', 'https://example.com/chips.jpg', 'cat-7'),
('sub-25', 'Chocolates', 'Premium chocolates and candies', 'https://example.com/chocolates.jpg', 'cat-7'),
('sub-26', 'Nuts & Seeds', 'Healthy nuts and seeds', 'https://example.com/nuts-seeds.jpg', 'cat-7'),

-- Health & Wellness subcategories
('sub-27', 'Organic Foods', 'Certified organic food products', 'https://example.com/organic-foods.jpg', 'cat-8'),
('sub-28', 'Supplements', 'Health supplements and vitamins', 'https://example.com/supplements.jpg', 'cat-8'),
('sub-29', 'Gluten-Free', 'Gluten-free food products', 'https://example.com/gluten-free.jpg', 'cat-8');

-- Insert sample data into product_new
INSERT INTO product_new (id, name, description, price, image_url, quantity, brand, unit, pua, pub, puc, item_unit, is_active, subcategory_id) VALUES

-- Fresh Fruits products
('prod-1', 'Red Apples', 'Fresh red apples from local orchards', 299.00, 'https://example.com/red-apples.jpg', 50, 'Farm Fresh', 'kg', 0.500, 1.000, 2.000, '{"unit_type": "weight", "base_unit": "kg", "display_name": "Red Apples", "variants": [{"size": "500g", "price": 149.50, "weight": 0.5}, {"size": "1kg", "price": 299.00, "weight": 1.0}, {"size": "2kg", "price": 580.00, "weight": 2.0}], "nutritional_info": {"calories_per_100g": 52, "protein": 0.3, "carbs": 14, "fiber": 2.4}}', TRUE, 'sub-1'),
('prod-2', 'Bananas', 'Ripe yellow bananas', 149.00, 'https://example.com/bananas.jpg', 30, 'Tropical', 'kg', 0.500, 1.000, NULL, '{"unit_type": "weight", "base_unit": "kg", "display_name": "Bananas", "variants": [{"size": "500g", "price": 74.50, "weight": 0.5}, {"size": "1kg", "price": 149.00, "weight": 1.0}], "nutritional_info": {"calories_per_100g": 89, "protein": 1.1, "carbs": 23, "fiber": 2.6}}', TRUE, 'sub-1'),
('prod-3', 'Orange Juice Oranges', 'Juicy oranges perfect for juice', 399.00, 'https://example.com/oranges.jpg', 25, 'Citrus Co', 'kg', 1.000, 2.000, 5.000, '{"unit_type": "weight", "base_unit": "kg", "display_name": "Oranges", "variants": [{"size": "1kg", "price": 399.00, "weight": 1.0}, {"size": "2kg", "price": 750.00, "weight": 2.0}, {"size": "5kg", "price": 1800.00, "weight": 5.0}], "nutritional_info": {"calories_per_100g": 47, "protein": 0.9, "carbs": 12, "fiber": 2.4, "vitamin_c": "92% DV"}}', TRUE, 'sub-1'),
('prod-4', 'Fresh Strawberries', 'Sweet strawberries', 549.00, 'https://example.com/strawberries.jpg', 15, 'Berry Best', 'pack', 0.250, 0.500, NULL, '{"unit_type": "pack", "base_unit": "pack", "display_name": "Strawberries", "variants": [{"size": "250g pack", "price": 274.50, "weight": 0.25}, {"size": "500g pack", "price": 549.00, "weight": 0.5}], "nutritional_info": {"calories_per_100g": 32, "protein": 0.7, "carbs": 8, "fiber": 2.0, "vitamin_c": "97% DV"}}', TRUE, 'sub-1'),

-- Fresh Vegetables products
('prod-5', 'Carrots', 'Fresh orange carrots', 199.00, 'https://example.com/carrots.jpg', 40, 'Garden Fresh', 'kg', 0.500, 1.000, 2.000, '{"unit_type": "weight", "base_unit": "kg", "display_name": "Carrots", "variants": [{"size": "500g", "price": 99.50, "weight": 0.5}, {"size": "1kg", "price": 199.00, "weight": 1.0}, {"size": "2kg", "price": 380.00, "weight": 2.0}], "nutritional_info": {"calories_per_100g": 41, "protein": 0.9, "carbs": 10, "fiber": 2.8, "vitamin_a": "334% DV"}}', TRUE, 'sub-2'),
('prod-6', 'Broccoli', 'Green broccoli heads', 349.00, 'https://example.com/broccoli.jpg', 20, 'Green Valley', 'piece', 0.300, 0.500, NULL, '{"unit_type": "piece", "base_unit": "piece", "display_name": "Broccoli", "variants": [{"size": "Small (300g)", "price": 209.40, "weight": 0.3}, {"size": "Medium (500g)", "price": 349.00, "weight": 0.5}], "nutritional_info": {"calories_per_100g": 34, "protein": 2.8, "carbs": 7, "fiber": 2.6, "vitamin_c": "149% DV"}}', TRUE, 'sub-2'),
('prod-7', 'Tomatoes', 'Ripe red tomatoes', 449.00, 'https://example.com/tomatoes.jpg', 35, 'Sunshine Farms', 'kg', 0.500, 1.000, 2.000, '{"unit_type": "weight", "base_unit": "kg", "display_name": "Tomatoes", "variants": [{"size": "500g", "price": 224.50, "weight": 0.5}, {"size": "1kg", "price": 449.00, "weight": 1.0}, {"size": "2kg", "price": 860.00, "weight": 2.0}], "nutritional_info": {"calories_per_100g": 18, "protein": 0.9, "carbs": 3.9, "fiber": 1.2, "vitamin_c": "21% DV", "lycopene": "high"}}', TRUE, 'sub-2'),
('prod-8', 'Potatoes', 'Russet potatoes', 249.00, 'https://example.com/potatoes.jpg', 60, 'Farm Fresh', 'kg', 1.000, 2.000, 5.000, '{"unit_type": "weight", "base_unit": "kg", "display_name": "Potatoes", "variants": [{"size": "1kg", "price": 249.00, "weight": 1.0}, {"size": "2kg", "price": 480.00, "weight": 2.0}, {"size": "5kg", "price": 1150.00, "weight": 5.0}], "nutritional_info": {"calories_per_100g": 77, "protein": 2.0, "carbs": 17, "fiber": 2.2}}', TRUE, 'sub-2'),

-- Organic Produce products
('prod-9', 'Organic Spinach', 'Certified organic spinach leaves', 599.00, 'https://example.com/organic-spinach.jpg', 12, 'Pure Organic', 'bunch', 0.200, 0.300, NULL, '{"unit_type": "bunch", "base_unit": "bunch", "display_name": "Organic Spinach", "variants": [{"size": "Small bunch (200g)", "price": 399.33, "weight": 0.2}, {"size": "Regular bunch (300g)", "price": 599.00, "weight": 0.3}], "nutritional_info": {"calories_per_100g": 23, "protein": 2.9, "carbs": 3.6, "iron": "15% DV", "organic": true}}', TRUE, 'sub-3'),
('prod-10', 'Organic Kale', 'Fresh organic kale', 649.00, 'https://example.com/organic-kale.jpg', 10, 'Pure Organic', 'bunch', 0.200, 0.400, NULL, '{"unit_type": "bunch", "base_unit": "bunch", "display_name": "Organic Kale", "variants": [{"size": "Small bunch (200g)", "price": 324.50, "weight": 0.2}, {"size": "Large bunch (400g)", "price": 649.00, "weight": 0.4}], "nutritional_info": {"calories_per_100g": 49, "protein": 4.3, "carbs": 9, "vitamin_k": "684% DV", "organic": true}}', TRUE, 'sub-3'),

-- Exotic Fruits products  
('prod-11', 'Dragon Fruit', 'Exotic dragon fruit', 999.00, 'https://example.com/dragon-fruit.jpg', 8, 'Exotic Imports', 'piece', 0.300, 0.500, NULL, '{"unit_type": "piece", "base_unit": "piece", "display_name": "Dragon Fruit", "variants": [{"size": "Small (300g)", "price": 599.40, "weight": 0.3}, {"size": "Large (500g)", "price": 999.00, "weight": 0.5}], "nutritional_info": {"calories_per_100g": 60, "protein": 1.2, "carbs": 13, "vitamin_c": "34% DV", "antioxidants": "high"}}', TRUE, 'sub-4'),
('prod-12', 'Passion Fruit', 'Tropical passion fruit', 799.00, 'https://example.com/passion-fruit.jpg', 15, 'Tropical Delights', 'piece', 0.100, 0.150, NULL, '{"unit_type": "piece", "base_unit": "piece", "display_name": "Passion Fruit", "variants": [{"size": "Small (100g)", "price": 532.67, "weight": 0.1}, {"size": "Large (150g)", "price": 799.00, "weight": 0.15}], "nutritional_info": {"calories_per_100g": 97, "protein": 2.2, "carbs": 23, "fiber": 10.4, "vitamin_a": "25% DV"}}', TRUE, 'sub-4'),

-- Milk & Cream products (continued)
('prod-14', 'Heavy Cream', 'Rich heavy cream', 549.00, 'https://example.com/heavy-cream.jpg', 25, 'Dairy Best', 'liter', 0.200, 0.500, 1.000, '{"unit_type": "volume", "base_unit": "liter", "display_name": "Heavy Cream", "variants": [{"size": "200ml", "price": 109.80, "volume": 0.2}, {"size": "500ml", "price": 274.50, "volume": 0.5}, {"size": "1L", "price": 549.00, "volume": 1.0}], "nutritional_info": {"calories_per_100ml": 345, "fat": 35, "protein": 2.8, "carbs": 2.8}}', TRUE, 'sub-5'),
('prod-15', 'Skim Milk', 'Low-fat skim milk', 349.00, 'https://example.com/skim-milk.jpg', 40, 'Healthy Choice', 'liter', 0.500, 1.000, 2.000, '{"unit_type": "volume", "base_unit": "liter", "display_name": "Skim Milk", "variants": [{"size": "500ml", "price": 174.50, "volume": 0.5}, {"size": "1L", "price": 349.00, "volume": 1.0}, {"size": "2L", "price": 664.00, "volume": 2.0}], "nutritional_info": {"calories_per_100ml": 34, "fat": 0.1, "protein": 3.4, "carbs": 5.0}}', TRUE, 'sub-5'),

-- Cheese products
('prod-16', 'Cheddar Cheese', 'Aged cheddar cheese', 699.00, 'https://example.com/cheddar-cheese.jpg', 30, 'Artisan Cheese', 'kg', TRUE, 'sub-6'),
('prod-17', 'Mozzarella Cheese', 'Fresh mozzarella', 599.00, 'https://example.com/mozzarella.jpg', 25, 'Italian Style', 'kg', TRUE, 'sub-6'),
('prod-18', 'Parmesan Cheese', 'Grated parmesan cheese', 999.00, 'https://example.com/parmesan.jpg', 20, 'Premium Select', 'kg', TRUE, 'sub-6'),

-- Yogurt products
('prod-19', 'Greek Yogurt', 'Thick Greek yogurt', 459.00, 'https://example.com/greek-yogurt.jpg', 35, 'Mediterranean', 'pack', TRUE, 'sub-7'),
('prod-20', 'Strawberry Yogurt', 'Strawberry flavored yogurt', 399.00, 'https://example.com/strawberry-yogurt.jpg', 40, 'Creamy Delight', 'pack', TRUE, 'sub-7'),

-- Eggs products
('prod-21', 'Free Range Eggs', 'Free range chicken eggs', 549.00, 'https://example.com/free-range-eggs.jpg', 45, 'Happy Hens', 'dozen', TRUE, 'sub-8'),
('prod-22', 'Organic Eggs', 'Certified organic eggs', 699.00, 'https://example.com/organic-eggs.jpg', 30, 'Pure Organic', 'dozen', TRUE, 'sub-8'),

-- Chicken products
('prod-23', 'Chicken Breast', 'Boneless chicken breast', 1199.00, 'https://example.com/chicken-breast.jpg', 25, 'Premium Poultry', 'kg', TRUE, 'sub-9'),
('prod-24', 'Whole Chicken', 'Fresh whole chicken', 999.00, 'https://example.com/whole-chicken.jpg', 20, 'Farm Fresh', 'piece', TRUE, 'sub-9'),

-- Beef products
('prod-25', 'Ground Beef', 'Lean ground beef', 1299.00, 'https://example.com/ground-beef.jpg', 30, 'Prime Cuts', 'kg', TRUE, 'sub-10'),
('prod-26', 'Ribeye Steak', 'Premium ribeye steak', 2399.00, 'https://example.com/ribeye-steak.jpg', 15, 'Premium Cuts', 'kg', TRUE, 'sub-10'),

-- Fish products
('prod-27', 'Fresh Salmon', 'Atlantic salmon fillet', 1999.00, 'https://example.com/salmon.jpg', 18, 'Ocean Fresh', 'kg', TRUE, 'sub-11'),
('prod-28', 'Tuna Steak', 'Fresh tuna steak', 2649.00, 'https://example.com/tuna-steak.jpg', 12, 'Premium Seafood', 'kg', TRUE, 'sub-11'),

-- Bread products
('prod-29', 'Whole Wheat Bread', 'Fresh whole wheat bread', 399.00, 'https://example.com/wheat-bread.jpg', 40, 'Bakery Fresh', 'loaf', NULL, NULL, NULL, '{"unit_type": "piece", "base_unit": "loaf", "display_name": "Whole Wheat Bread", "variants": [{"size": "Small Loaf (400g)", "price": 299.00, "weight": 0.4}, {"size": "Regular Loaf (600g)", "price": 399.00, "weight": 0.6}, {"size": "Large Loaf (800g)", "price": 489.00, "discounted_price": 449.00, "weight": 0.8}], "nutritional_info": {"calories_per_100g": 247, "protein": 13, "carbs": 41, "fiber": 7, "whole_grain": true}}', TRUE, 'sub-13'),
('prod-30', 'Sourdough Bread', 'Artisan sourdough bread', 549.00, 'https://example.com/sourdough.jpg', 25, 'Artisan Bakery', 'loaf', NULL, NULL, NULL, '{"unit_type": "piece", "base_unit": "loaf", "display_name": "Sourdough Bread", "variants": [{"size": "Small Loaf (350g)", "price": 449.00, "weight": 0.35}, {"size": "Regular Loaf (500g)", "price": 549.00, "weight": 0.5}, {"size": "Large Loaf (750g)", "price": 689.00, "discounted_price": 599.00, "weight": 0.75}], "nutritional_info": {"calories_per_100g": 230, "protein": 9, "carbs": 48, "fiber": 3, "sourdough": true}}', TRUE, 'sub-13'),

-- Orange Juice products
('prod-33', 'Fresh Orange Juice', 'Freshly squeezed orange juice', 639.00, 'https://example.com/orange-juice.jpg', 35, 'Fresh Squeeze', 'liter', TRUE, 'sub-16'),
('prod-34', 'Apple Juice', 'Pure apple juice', 559.00, 'https://example.com/apple-juice.jpg', 40, 'Orchard Fresh', 'liter', TRUE, 'sub-16'),

-- Soft Drinks products
('prod-35', 'Cola', 'Classic cola drink', 319.00, 'https://example.com/cola.jpg', 60, 'Classic Cola', 'liter', 0.330, 0.600, 1.500, '{"unit_type": "volume", "base_unit": "liter", "display_name": "Cola", "variants": [{"size": "330ml", "price": 105.27, "volume": 0.33}, {"size": "600ml", "price": 191.40, "volume": 0.6}, {"size": "1.5L", "price": 478.50, "volume": 1.5}], "nutritional_info": {"calories_per_100ml": 42, "sugar": 10.6, "caffeine": "10mg"}}', TRUE, 'sub-17'),

-- Coffee & Tea products
('prod-37', 'Premium Coffee Beans', 'Arabica coffee beans', 1279.00, 'https://example.com/coffee-beans.jpg', 25, 'Coffee Master', 'kg', TRUE, 'sub-18'),
('prod-38', 'Earl Grey Tea', 'Premium Earl Grey tea', 999.00, 'https://example.com/earl-grey.jpg', 30, 'Tea House', 'pack', TRUE, 'sub-18'),

-- Rice & Grains products
('prod-39', 'Basmati Rice', 'Premium basmati rice', 719.00, 'https://example.com/basmati-rice.jpg', 45, 'Golden Grain', 'kg', 1.000, 5.000, 10.000, '{"unit_type": "weight", "base_unit": "kg", "display_name": "Basmati Rice", "variants": [{"size": "1kg", "price": 719.00, "weight": 1.0}, {"size": "5kg", "price": 3400.00, "weight": 5.0}, {"size": "10kg", "price": 6500.00, "weight": 10.0}], "nutritional_info": {"calories_per_100g": 130, "protein": 2.7, "carbs": 28, "fiber": 0.4}, "cooking_instructions": "Rinse rice, add 1.5 cups water per cup rice, boil for 18-20 minutes"}', TRUE, 'sub-20'),

-- Chips & Crisps products
('prod-41', 'Potato Chips', 'Classic salted potato chips', 399.00, 'https://example.com/potato-chips.jpg', 55, 'Crispy Crunch', 'pack', TRUE, 'sub-24'),
('prod-42', 'Tortilla Chips', 'Corn tortilla chips', 439.00, 'https://example.com/tortilla-chips.jpg', 40, 'Mexican Style', 'pack', TRUE, 'sub-24'),

-- Chocolates products
('prod-43', 'Dark Chocolate', 'Premium dark chocolate', 799.00, 'https://example.com/dark-chocolate.jpg', 35, 'Choco Deluxe', 'bar', TRUE, 'sub-25'),
('prod-44', 'Milk Chocolate', 'Creamy milk chocolate', 639.00, 'https://example.com/milk-chocolate.jpg', 40, 'Sweet Dreams', 'bar', TRUE, 'sub-25'),

-- Nuts & Seeds products
('prod-45', 'Mixed Nuts', 'Premium mixed nuts', 999.00, 'https://example.com/mixed-nuts.jpg', 25, 'Nutri Best', 'pack', TRUE, 'sub-26'),
('prod-46', 'Almonds', 'Raw almonds', 1279.00, 'https://example.com/almonds.jpg', 30, 'Healthy Nuts', 'kg', TRUE, 'sub-26');

-- Create a view to easily see the category hierarchy
CREATE VIEW category_hierarchy AS
SELECT 
    c.id as category_id,
    c.name as category_name,
    s.id as subcategory_id,
    s.name as subcategory_name,
    p.id as product_id,
    p.name as product_name,
    p.price,
    p.quantity,
    p.is_active
FROM category_new c
LEFT JOIN subcategory s ON c.id = s.category_new_id
LEFT JOIN product_new p ON s.id = p.subcategory_id
ORDER BY c.name, s.name, p.name;

-- Show summary statistics
SELECT 'Categories' as table_name, COUNT(*) as record_count FROM category_new
UNION ALL
SELECT 'Subcategories' as table_name, COUNT(*) as record_count FROM subcategory
UNION ALL
SELECT 'Products' as table_name, COUNT(*) as record_count FROM product_new;

COMMIT;