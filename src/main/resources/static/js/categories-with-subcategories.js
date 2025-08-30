// All Categories with Subcategories Display

// Function to load and display all categories with their subcategories
function loadAllCategoriesWithSubcategories() {
    console.log('Loading all categories with subcategories...');
    
    const container = document.getElementById('categoriesWithSubcategories');
    
    // Show loading state
    container.innerHTML = `
        <div class="loading-message text-center py-5">
            <i class="fas fa-spinner fa-spin fa-2x mb-3"></i>
            <p>Loading categories and subcategories...</p>
        </div>
    `;
    
    // Fetch all categories with their subcategories
    fetch('/api/categories/with-subcategories')
        .then(response => {
            console.log('Categories fetch response status:', response.status);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(categories => {
            console.log('Received categories:', categories);
            
            if (!categories || categories.length === 0) {
                container.innerHTML = `
                    <div class="text-center py-5">
                        <i class="fas fa-box-open fa-3x mb-3 text-muted"></i>
                        <h4>No Categories Found</h4>
                        <p class="text-muted">No categories are currently available.</p>
                    </div>
                `;
                return;
            }
            
            // Clear container
            container.innerHTML = '';
            
            // Create sections for each category
            categories.forEach((category, index) => {
                const categorySection = createCategorySection(category, index);
                container.appendChild(categorySection);
            });
            
            console.log(`Created ${categories.length} category sections`);
        })
        .catch(error => {
            console.error('Error loading categories:', error);
            container.innerHTML = `
                <div class="text-center py-5">
                    <i class="fas fa-exclamation-triangle fa-3x mb-3 text-warning"></i>
                    <h4>Error Loading Categories</h4>
                    <p class="text-muted">Unable to load categories. Please refresh the page.</p>
                    <button class="btn btn-primary mt-3" onclick="loadAllCategoriesWithSubcategories()">
                        <i class="fas fa-refresh"></i> Retry
                    </button>
                </div>
            `;
        });
}

// Function to create a category section with its subcategories carousel
function createCategorySection(category, index) {
    const section = document.createElement('div');
    section.className = 'category-section';
    section.setAttribute('data-category-id', category.id);
    
    // Simple category title (Blinkit-style)
    const header = document.createElement('div');
    header.className = 'simple-category-title';
    header.innerHTML = `${category.name}`;
    
    section.appendChild(header);
    
    // Subcategories carousel
    const carouselWrapper = document.createElement('div');
    carouselWrapper.className = 'subcategory-carousel-wrapper';
    
    if (category.subcategories && category.subcategories.length > 0) {
        carouselWrapper.innerHTML = `
            <button class="btn carousel-nav-btn carousel-prev" onclick="navigateCarousel('${category.id}', 'prev')">
                <i class="fas fa-chevron-left"></i>
            </button>
            <div class="subcategory-track" id="track-${category.id}">
                <!-- Subcategory cards will be added here -->
            </div>
            <button class="btn carousel-nav-btn carousel-next" onclick="navigateCarousel('${category.id}', 'next')">
                <i class="fas fa-chevron-right"></i>
            </button>
        `;
        
        section.appendChild(carouselWrapper);
        
        // Add subcategory cards after a short delay to ensure DOM is ready
        setTimeout(() => {
            addSubcategoryCards(category.id, category.subcategories);
            updateCarouselNavigation(category.id);
        }, 100);
    } else {
        carouselWrapper.innerHTML = `
            <div class="no-subcategories">
                <i class="fas fa-info-circle mb-2"></i>
                <p>No subcategories available for this category.</p>
            </div>
        `;
        section.appendChild(carouselWrapper);
    }
    
    return section;
}

// Function to add subcategory cards to a track
function addSubcategoryCards(categoryId, subcategories) {
    const track = document.getElementById(`track-${categoryId}`);
    if (!track) return;
    
    // Get the category name from the section
    const section = document.querySelector(`[data-category-id="${categoryId}"]`);
    const categoryName = section ? section.querySelector('.simple-category-title').textContent : '';
    
    track.innerHTML = ''; // Clear existing cards
    
    subcategories.forEach(subcategory => {
        const card = createSubcategoryCard(subcategory, categoryId, categoryName);
        track.appendChild(card);
    });
    
    console.log(`Added ${subcategories.length} subcategory cards for category ${categoryId}`);
}

// Function to create a subcategory card
function createSubcategoryCard(subcategory, categoryId, categoryName) {
    const card = document.createElement('div');
    card.className = 'subcategory-card';
    card.setAttribute('data-subcategory-id', subcategory.id);
    card.setAttribute('data-subcategory-name', subcategory.name);
    card.setAttribute('data-category-id', categoryId);
    card.setAttribute('data-category-name', categoryName);
    
    // Use the same Grofers image for all subcategory cards
    const imageUrl = 'https://cdn.grofers.com/cdn-cgi/image/f=auto,fit=scale-down,q=70,metadata=none,w=360/app/images/category/cms_images/icon/1487_1679466558536.png';
    
    card.innerHTML = `
        <div class="subcategory-image">
            <img src="${imageUrl}" 
                 alt="${subcategory.name}" 
                 loading="lazy"
                 onerror="this.src='https://cdn.grofers.com/cdn-cgi/image/f=auto,fit=scale-down,q=70,metadata=none,w=360/app/images/category/cms_images/icon/1487_1679466558536.png'">
        </div>
        <div class="subcategory-content">
            <h6 class="subcategory-name">${subcategory.name}</h6>
            <p class="subcategory-description">${subcategory.description || "Explore products in this category"}</p>
            <div class="subcategory-product-count" data-subcategory-id="${subcategory.id}">
                <span class="loading-dots">
                    <i class="fas fa-spinner fa-spin"></i> Loading...
                </span>
            </div>
        </div>
    `;
    
    // Add click handler
    card.addEventListener('click', function() {
        // Visual feedback
        this.style.transform = 'scale(0.95)';
        setTimeout(() => {
            this.style.transform = '';
        }, 150);
        
        // Navigate to shopping page with category and subcategory parameters
        setTimeout(() => {
            navigateToProducts(subcategory.id, subcategory.name, categoryId, categoryName);
        }, 200);
    });
    
    // Load product count asynchronously
    loadProductCountForSubcategory(subcategory.id);
    
    return card;
}

// Function to load product count for a subcategory
function loadProductCountForSubcategory(subcategoryId) {
    fetch(`/api/products/subcategory/${subcategoryId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(products => {
            const countElement = document.querySelector(`.subcategory-product-count[data-subcategory-id="${subcategoryId}"]`);
            if (countElement) {
                const productCount = products.length;
                countElement.innerHTML = `
                    <span>
                        <i class="fas fa-box"></i> Products
                    </span>
                    <span class="product-count">${productCount}</span>
                `;
            }
        })
        .catch(error => {
            console.error(`Error loading product count for subcategory ${subcategoryId}:`, error);
            const countElement = document.querySelector(`.subcategory-product-count[data-subcategory-id="${subcategoryId}"]`);
            if (countElement) {
                countElement.innerHTML = `
                    <span class="error">
                        <i class="fas fa-exclamation-triangle"></i> Error loading
                    </span>
                `;
            }
        });
}

// Function to navigate to shopping page with category and subcategory parameters
function navigateToProducts(subcategoryId, subcategoryName, categoryId, categoryName) {
    console.log(`Navigating to shopping page for subcategory: ${subcategoryName} (ID: ${subcategoryId}) in category: ${categoryName} (ID: ${categoryId})`);
    
    // Construct the URL in the format: /categories/shopping?categoryId=cat-1&categoryName=Fruits+%26+Vegetables&subcategoryId=sub-4&subcategoryName=Exotic%2520Fruits
    const url = `/categories/shopping?categoryId=${categoryId}&categoryName=${encodeURIComponent(categoryName)}&subcategoryId=${subcategoryId}&subcategoryName=${encodeURIComponent(subcategoryName)}`;
    
    // Navigate to the shopping page (opens in same tab to match your requirement)
    window.location.href = url;
}

// Function to navigate carousel
function navigateCarousel(categoryId, direction) {
    const track = document.getElementById(`track-${categoryId}`);
    if (!track) return;
    
    const cards = track.querySelectorAll('.subcategory-card');
    if (cards.length === 0) return;
    
    const cardWidth = cards[0].offsetWidth + 20; // card width + gap
    const containerWidth = track.parentElement.offsetWidth - 120; // subtract nav buttons
    const visibleCards = Math.floor(containerWidth / cardWidth);
    const maxScroll = Math.max(0, (cards.length - visibleCards) * cardWidth);
    
    const currentTransform = track.style.transform;
    const currentScroll = currentTransform ? parseInt(currentTransform.match(/-?\d+/)?.[0] || '0') : 0;
    
    let newScroll;
    if (direction === 'prev') {
        newScroll = Math.min(0, currentScroll + (cardWidth * 2));
    } else {
        newScroll = Math.max(-maxScroll, currentScroll - (cardWidth * 2));
    }
    
    track.style.transform = `translateX(${newScroll}px)`;
    updateCarouselNavigation(categoryId);
}

// Function to update carousel navigation buttons
function updateCarouselNavigation(categoryId) {
    const track = document.getElementById(`track-${categoryId}`);
    if (!track) return;
    
    const cards = track.querySelectorAll('.subcategory-card');
    if (cards.length === 0) return;
    
    const cardWidth = cards[0].offsetWidth + 20;
    const containerWidth = track.parentElement.offsetWidth - 120;
    const visibleCards = Math.floor(containerWidth / cardWidth);
    const maxScroll = Math.max(0, (cards.length - visibleCards) * cardWidth);
    
    const currentTransform = track.style.transform;
    const currentScroll = Math.abs(currentTransform ? parseInt(currentTransform.match(/-?\d+/)?.[0] || '0') : 0);
    
    const section = document.querySelector(`[data-category-id="${categoryId}"]`);
    if (!section) return;
    
    const prevBtn = section.querySelector('.carousel-prev');
    const nextBtn = section.querySelector('.carousel-next');
    
    if (currentScroll <= 0) {
        prevBtn.classList.add('disabled');
    } else {
        prevBtn.classList.remove('disabled');
    }
    
    if (currentScroll >= maxScroll) {
        nextBtn.classList.add('disabled');
    } else {
        nextBtn.classList.remove('disabled');
    }
}

// Initial load
document.addEventListener('DOMContentLoaded', function() {
    loadAllCategoriesWithSubcategories();
});