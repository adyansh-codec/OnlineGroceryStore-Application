-- Simple Data Insertion Script for New Category System
-- Use this script to insert data into the already created tables

-- Insert Categories
INSERT INTO category_new (id, name, description, image_url) VALUES
('cat-fruits-veg', 'Fruits & Vegetables', 'Fresh fruits and vegetables from local farms', 'https://example.com/fruits-vegetables.jpg'),
('cat-dairy', 'Dairy & Eggs', 'Fresh dairy products and farm eggs', 'https://example.com/dairy-eggs.jpg'),
('cat-meat', 'Meat & Seafood', 'Premium quality meat and fresh seafood', 'https://example.com/meat-seafood.jpg'),
('cat-bakery', 'Bakery', 'Fresh baked goods and bread', 'https://example.com/bakery.jpg'),
('cat-beverages', 'Beverages', 'Refreshing drinks and beverages', 'https://example.com/beverages.jpg'),
('cat-pantry', 'Pantry Staples', 'Essential cooking ingredients and pantry items', 'https://example.com/pantry.jpg'),
('cat-snacks', 'Snacks & Confectionery', 'Delicious snacks and sweet treats', 'https://example.com/snacks.jpg');

-- Insert Subcategories
INSERT INTO subcategory (id, name, description, image_url, category_new_id) VALUES
-- Fruits & Vegetables
('sub-fresh-fruits', 'Fresh Fruits', 'Seasonal fresh fruits', 'https://example.com/fresh-fruits.jpg', 'cat-fruits-veg'),
('sub-vegetables', 'Fresh Vegetables', 'Farm-fresh vegetables', 'https://example.com/fresh-vegetables.jpg', 'cat-fruits-veg'),
('sub-organic', 'Organic Produce', 'Certified organic fruits and vegetables', 'https://example.com/organic-produce.jpg', 'cat-fruits-veg'),

-- Dairy & Eggs
('sub-milk', 'Milk & Cream', 'Fresh milk and dairy products', 'https://example.com/milk-cream.jpg', 'cat-dairy'),
('sub-cheese', 'Cheese', 'Variety of fresh and aged cheeses', 'https://example.com/cheese.jpg', 'cat-dairy'),
('sub-eggs', 'Eggs', 'Farm-fresh eggs', 'https://example.com/eggs.jpg', 'cat-dairy'),

-- Meat & Seafood
('sub-chicken', 'Chicken', 'Fresh chicken and poultry', 'https://example.com/chicken.jpg', 'cat-meat'),
('sub-beef', 'Beef', 'Premium beef cuts', 'https://example.com/beef.jpg', 'cat-meat'),
('sub-fish', 'Fish', 'Fresh fish and seafood', 'https://example.com/fish.jpg', 'cat-meat'),

-- Bakery
('sub-bread', 'Bread', 'Fresh baked bread', 'https://example.com/bread.jpg', 'cat-bakery'),
('sub-pastries', 'Pastries', 'Sweet and savory pastries', 'https://example.com/pastries.jpg', 'cat-bakery'),

-- Beverages
('sub-juices', 'Juices', 'Fresh and packaged juices', 'https://example.com/juices.jpg', 'cat-beverages'),
('sub-soft-drinks', 'Soft Drinks', 'Carbonated and non-carbonated drinks', 'https://example.com/soft-drinks.jpg', 'cat-beverages'),

-- Pantry Staples
('sub-grains', 'Rice & Grains', 'Various types of rice and grains', 'https://example.com/rice-grains.jpg', 'cat-pantry'),
('sub-spices', 'Spices & Seasonings', 'Herbs, spices, and seasonings', 'https://example.com/spices.jpg', 'cat-pantry'),

-- Snacks
('sub-chips', 'Chips & Crisps', 'Various flavored chips and crisps', 'https://example.com/chips.jpg', 'cat-snacks'),
('sub-chocolate', 'Chocolates', 'Premium chocolates and candies', 'https://example.com/chocolates.jpg', 'cat-snacks');

-- Insert Products
INSERT INTO product_new (id, name, description, price, image_url, quantity, brand, unit, is_active, subcategory_id) VALUES

-- Fresh Fruits
('prod-apples', 'Red Apples', 'Fresh red apples from local orchards', 3.99, 'https://example.com/red-apples.jpg', 50, 'Farm Fresh', 'kg', TRUE, 'sub-fresh-fruits'),
('prod-bananas', 'Bananas', 'Ripe yellow bananas', 2.49, 'https://example.com/bananas.jpg', 30, 'Tropical', 'kg', TRUE, 'sub-fresh-fruits'),
('prod-oranges', 'Oranges', 'Juicy oranges perfect for juice', 4.99, 'https://example.com/oranges.jpg', 25, 'Citrus Co', 'kg', TRUE, 'sub-fresh-fruits'),
('prod-strawberries', 'Strawberries', 'Sweet strawberries', 6.99, 'https://example.com/strawberries.jpg', 15, 'Berry Best', 'pack', TRUE, 'sub-fresh-fruits'),

-- Fresh Vegetables
('prod-carrots', 'Carrots', 'Fresh orange carrots', 2.99, 'https://example.com/carrots.jpg', 40, 'Garden Fresh', 'kg', TRUE, 'sub-vegetables'),
('prod-broccoli', 'Broccoli', 'Green broccoli heads', 4.49, 'https://example.com/broccoli.jpg', 20, 'Green Valley', 'piece', TRUE, 'sub-vegetables'),
('prod-tomatoes', 'Tomatoes', 'Ripe red tomatoes', 5.99, 'https://example.com/tomatoes.jpg', 35, 'Sunshine Farms', 'kg', TRUE, 'sub-vegetables'),
('prod-potatoes', 'Potatoes', 'Russet potatoes', 3.49, 'https://example.com/potatoes.jpg', 60, 'Farm Fresh', 'kg', TRUE, 'sub-vegetables'),

-- Organic Produce
('prod-organic-spinach', 'Organic Spinach', 'Certified organic spinach leaves', 7.99, 'https://example.com/organic-spinach.jpg', 12, 'Pure Organic', 'bunch', TRUE, 'sub-organic'),
('prod-organic-kale', 'Organic Kale', 'Fresh organic kale', 8.49, 'https://example.com/organic-kale.jpg', 10, 'Pure Organic', 'bunch', TRUE, 'sub-organic'),

-- Milk & Cream
('prod-whole-milk', 'Whole Milk', 'Fresh whole milk', 4.99, 'https://example.com/whole-milk.jpg', 50, 'Dairy Best', 'liter', TRUE, 'sub-milk'),
('prod-skim-milk', 'Skim Milk', 'Low-fat skim milk', 4.49, 'https://example.com/skim-milk.jpg', 40, 'Healthy Choice', 'liter', TRUE, 'sub-milk'),

-- Cheese
('prod-cheddar', 'Cheddar Cheese', 'Aged cheddar cheese', 8.99, 'https://example.com/cheddar-cheese.jpg', 30, 'Artisan Cheese', 'kg', TRUE, 'sub-cheese'),
('prod-mozzarella', 'Mozzarella Cheese', 'Fresh mozzarella', 7.49, 'https://example.com/mozzarella.jpg', 25, 'Italian Style', 'kg', TRUE, 'sub-cheese'),

-- Eggs
('prod-free-range-eggs', 'Free Range Eggs', 'Free range chicken eggs', 6.99, 'https://example.com/free-range-eggs.jpg', 45, 'Happy Hens', 'dozen', TRUE, 'sub-eggs'),

-- Chicken
('prod-chicken-breast', 'Chicken Breast', 'Boneless chicken breast', 14.99, 'https://example.com/chicken-breast.jpg', 25, 'Premium Poultry', 'kg', TRUE, 'sub-chicken'),
('prod-whole-chicken', 'Whole Chicken', 'Fresh whole chicken', 12.99, 'https://example.com/whole-chicken.jpg', 20, 'Farm Fresh', 'piece', TRUE, 'sub-chicken'),

-- Beef
('prod-ground-beef', 'Ground Beef', 'Lean ground beef', 16.99, 'https://example.com/ground-beef.jpg', 30, 'Prime Cuts', 'kg', TRUE, 'sub-beef'),

-- Fish
('prod-salmon', 'Fresh Salmon', 'Atlantic salmon fillet', 24.99, 'https://example.com/salmon.jpg', 18, 'Ocean Fresh', 'kg', TRUE, 'sub-fish'),

-- Bread
('prod-wheat-bread', 'Whole Wheat Bread', 'Fresh whole wheat bread', 4.99, 'https://example.com/wheat-bread.jpg', 40, 'Bakery Fresh', 'loaf', TRUE, 'sub-bread'),
('prod-sourdough', 'Sourdough Bread', 'Artisan sourdough bread', 6.99, 'https://example.com/sourdough.jpg', 25, 'Artisan Bakery', 'loaf', TRUE, 'sub-bread'),

-- Pastries
('prod-croissants', 'Croissants', 'Buttery croissants', 8.99, 'https://example.com/croissants.jpg', 30, 'French Bakery', 'pack', TRUE, 'sub-pastries'),

-- Juices
('prod-orange-juice', 'Orange Juice', 'Freshly squeezed orange juice', 7.99, 'https://example.com/orange-juice.jpg', 35, 'Fresh Squeeze', 'liter', TRUE, 'sub-juices'),
('prod-apple-juice', 'Apple Juice', 'Pure apple juice', 6.99, 'https://example.com/apple-juice.jpg', 40, 'Orchard Fresh', 'liter', TRUE, 'sub-juices'),

-- Soft Drinks
('prod-cola', 'Cola', 'Classic cola drink', 3.99, 'https://example.com/cola.jpg', 60, 'Classic Cola', 'liter', TRUE, 'sub-soft-drinks'),

-- Rice & Grains
('prod-basmati-rice', 'Basmati Rice', 'Premium basmati rice', 8.99, 'https://example.com/basmati-rice.jpg', 45, 'Golden Grain', 'kg', TRUE, 'sub-grains'),

-- Chips
('prod-potato-chips', 'Potato Chips', 'Classic salted potato chips', 4.99, 'https://example.com/potato-chips.jpg', 55, 'Crispy Crunch', 'pack', TRUE, 'sub-chips'),

-- Chocolate
('prod-dark-chocolate', 'Dark Chocolate', 'Premium dark chocolate', 9.99, 'https://example.com/dark-chocolate.jpg', 35, 'Choco Deluxe', 'bar', TRUE, 'sub-chocolate');
