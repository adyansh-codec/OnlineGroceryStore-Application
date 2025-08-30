# Data Entry Application - Usage Guide

## Overview
This application provides a comprehensive interface to manage categories, subcategories, and products in the Online Grocery Store database. You can create, edit, and delete items across all three tables in a hierarchical manner.

## Access
Navigate to: `http://localhost:8080/admin/data-entry`

## Features

### 1. **Category Management**
- **Create**: Add new product categories
- **Edit**: Modify existing category details
- **Delete**: Remove categories (this will also delete all subcategories and products within)
- **View**: See all categories with their subcategories and products in a hierarchical view

### 2. **Subcategory Management**
- **Create**: Add subcategories under existing categories
- **Edit**: Modify subcategory details
- **Delete**: Remove subcategories (this will also delete all products within)
- **Link**: Each subcategory must belong to a parent category

### 3. **Product Management**
- **Create**: Add products under existing subcategories
- **Edit**: Modify product details including pricing, inventory, and descriptions
- **Delete**: Remove individual products
- **Features**: 
  - Price and quantity management
  - Brand and unit specifications
  - Image URL support with preview
  - Active/inactive status toggle

## Database Structure

### Tables Created:
1. **category_new**: Main product categories
2. **subcategory**: Product subcategories (linked to categories)
3. **product_new**: Individual products (linked to subcategories)

### Relationships:
- Category → Subcategory (One-to-Many)
- Subcategory → Product (One-to-Many)

## How to Use

### Step 1: Create Categories
1. Click "Add Category" button
2. Fill in category name (required)
3. Add description and image URL (optional)
4. Click "Create Category"

### Step 2: Add Subcategories
1. Click "Add Subcategory" button or "Add Sub" next to a category
2. Select parent category from dropdown
3. Fill in subcategory details
4. Click "Create Subcategory"

### Step 3: Add Products
1. Click "Add Product" button or "+" next to a subcategory
2. Select category and subcategory
3. Fill in product details:
   - Name (required)
   - Price (required)
   - Quantity (required)
   - Description, brand, unit, image URL (optional)
   - Active status checkbox
4. Click "Create Product"

## Sample Data
The system comes with a complete SQL script (`insert-data.sql`) that includes:
- 8 categories (Fruits & Vegetables, Dairy & Eggs, etc.)
- 29 subcategories
- 46 sample products

## Features of the Interface

### Visual Hierarchy
- **Categories**: Blue left border, folder icon
- **Subcategories**: Green left border, indented, folder-open icon
- **Products**: Yellow left border, further indented, box icon

### Smart Controls
- **Dynamic dropdowns**: Subcategories load based on selected category
- **Image preview**: See images before saving
- **Validation**: Prevents duplicate names within the same parent
- **Bulk operations**: Delete categories removes all children

### AJAX Features
- Real-time subcategory loading
- Dynamic product filtering
- No page refresh for dropdown updates

## API Endpoints

### Web Interface:
- `GET /admin/data-entry` - Main dashboard
- `GET /admin/data-entry/category/new` - Add category form
- `GET /admin/data-entry/subcategory/new` - Add subcategory form
- `GET /admin/data-entry/product/new` - Add product form

### AJAX API:
- `GET /admin/data-entry/api/subcategories/{categoryId}` - Get subcategories by category
- `GET /admin/data-entry/api/products/{subcategoryId}` - Get products by subcategory

## Technical Details

### Technologies Used:
- **Backend**: Spring Boot, JPA/Hibernate
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Database**: PostgreSQL
- **Validation**: Server-side validation with client-side feedback

### Security Features:
- Form validation
- Duplicate name prevention
- Cascade delete warnings
- Error handling and user feedback

## Tips for Best Results

1. **Start with categories**: Create your main product categories first
2. **Organize logically**: Group related items under appropriate subcategories
3. **Use good names**: Clear, descriptive names help customers find products
4. **Add images**: Visual appeal improves user experience
5. **Keep inventory updated**: Use the quantity field to track stock levels
6. **Use brands**: Brand information helps customers make choices

## Troubleshooting

### Common Issues:
1. **"Category already exists"**: Choose a different category name
2. **"Subcategory already exists"**: Subcategory names must be unique within each category
3. **"Product already exists"**: Product names must be unique within each subcategory
4. **Dropdown not loading**: Check that categories exist before adding subcategories

### Database Issues:
- Ensure PostgreSQL is running
- Check that tables were created successfully
- Verify foreign key relationships are intact

## Support
For technical issues or questions about the data entry system, check the application logs or contact the development team.
