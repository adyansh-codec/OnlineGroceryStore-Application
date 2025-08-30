-- Add product_details JSON column to product_new table
-- This column will store detailed product information as JSON

ALTER TABLE product_new ADD COLUMN product_details JSON DEFAULT NULL;

-- Create an index on the product_details column for better query performance
CREATE INDEX idx_product_new_details ON product_new USING GIN (product_details);

-- Add sample product_details data for existing products
UPDATE product_new SET product_details = '{
    "nutritional_info": {
        "calories_per_100g": 52,
        "protein": "0.3g",
        "carbs": "14g",
        "fiber": "2.4g",
        "sugar": "10.4g",
        "fat": "0.2g",
        "sodium": "1mg",
        "vitamin_c": "7% DV",
        "potassium": "107mg"
    },
    "ingredients": [
        "Fresh Red Apples",
        "Natural Preservatives (as required)"
    ],
    "storage_instructions": "Store in refrigerator. Best consumed within 7 days of purchase.",
    "origin": "Local Orchards, Farm Fresh Produce",
    "certifications": ["Fresh Produce Certified", "Quality Assured"],
    "allergen_info": {
        "contains": [],
        "may_contain": ["Traces of other fruits processed in same facility"]
    },
    "additional_info": [
        {
            "title": "About the Product",
            "content": "Fresh, crisp red apples sourced directly from local orchards. These apples are hand-picked at optimal ripeness to ensure the best taste and nutritional value."
        },
        {
            "title": "Benefits",
            "content": "Rich in fiber, vitamin C, and antioxidants. Supports digestive health and provides natural energy. Perfect for snacking or cooking."
        },
        {
            "title": "Usage Tips",
            "content": "Great for eating fresh, making apple pie, or adding to salads. Can be stored at room temperature for 2-3 days or refrigerated for longer freshness."
        },
        {
            "title": "Quality Guarantee",
            "content": "We ensure the highest quality by working directly with trusted local farmers. If you are not satisfied, please contact our customer service."
        }
    ]
}' WHERE id = 'prod-1';

UPDATE product_new SET product_details = '{
    "nutritional_info": {
        "calories_per_100g": 89,
        "protein": "1.1g",
        "carbs": "23g",
        "fiber": "2.6g",
        "sugar": "12.2g",
        "fat": "0.3g",
        "sodium": "1mg",
        "potassium": "358mg",
        "vitamin_b6": "20% DV"
    },
    "ingredients": [
        "Fresh Yellow Bananas"
    ],
    "storage_instructions": "Store at room temperature until ripe, then refrigerate. Best consumed within 5-7 days.",
    "origin": "Tropical Farms, Premium Quality",
    "certifications": ["Naturally Ripened", "Quality Assured"],
    "allergen_info": {
        "contains": [],
        "may_contain": ["Processed in facility that handles other fruits"]
    },
    "additional_info": [
        {
            "title": "About the Product",
            "content": "Premium quality yellow bananas, naturally ripened to perfection. Rich in potassium and natural sugars for instant energy."
        },
        {
            "title": "Health Benefits",
            "content": "Excellent source of potassium, vitamin B6, and dietary fiber. Supports heart health, muscle function, and digestive wellness."
        },
        {
            "title": "Perfect For",
            "content": "Smoothies, baking, breakfast cereals, or healthy snacking. Great for pre and post-workout nutrition."
        },
        {
            "title": "Ripeness Guide",
            "content": "Yellow with small brown spots indicates peak ripeness. Green tips mean they will ripen in 2-3 days at room temperature."
        }
    ]
}' WHERE id = 'prod-2';

-- Add default product_details for products that don't have specific data
UPDATE product_new SET product_details = '{
    "nutritional_info": {
        "calories_per_100g": 0,
        "protein": "N/A",
        "carbs": "N/A",
        "fiber": "N/A"
    },
    "ingredients": ["Natural ingredients"],
    "storage_instructions": "Store in a cool, dry place.",
    "origin": "Quality sourced",
    "certifications": ["Quality Assured"],
    "allergen_info": {
        "contains": [],
        "may_contain": []
    },
    "additional_info": [
        {
            "title": "About the Product",
            "content": "High-quality product sourced from trusted suppliers."
        },
        {
            "title": "Usage",
            "content": "Follow package instructions for best results."
        }
    ]
}' WHERE product_details IS NULL;