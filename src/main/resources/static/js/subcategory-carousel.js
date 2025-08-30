// Subcategory Carousel Functions for All Categories Loading

// Function to show subcategories for a selected category (immediate loading)
function showSubcategoriesForCategory(categoryName) {
    console.log('showSubcategoriesForCategory called with:', categoryName);
    const subcategoriesCarousel = document.getElementById("subcategoriesCarousel");
    
    // Check if we already have subcategories loaded for all categories
    if (window.allSubcategoriesData && window.allSubcategoriesData[categoryName]) {
        const subcategories = window.allSubcategoriesData[categoryName];
        displaySubcategories(categoryName, subcategories);
        return;
    }
    
    // If not loaded yet, fetch subcategories for the category from REST API
    fetch(`/api/subcategories/category/${encodeURIComponent(categoryName)}`)
        .then(response => {
            console.log('Fetch response status:', response.status);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(subcategories => {
            console.log('Received subcategories:', subcategories);
            if (!subcategories || subcategories.length === 0) {
                console.log('No subcategories found, hiding carousel');
                if (subcategoriesCarousel) {
                    subcategoriesCarousel.style.display = "none";
                }
                return;
            }
            
            // Store in cache for future use
            if (!window.allSubcategoriesData) {
                window.allSubcategoriesData = {};
            }
            window.allSubcategoriesData[categoryName] = subcategories;
            
            displaySubcategories(categoryName, subcategories);
        })
        .catch(error => {
            console.error('Error fetching subcategories:', error);
            // Hide carousel on error
            if (subcategoriesCarousel) {
                subcategoriesCarousel.style.display = "none";
            }
        });
}

// Function to preload all subcategories for all categories
function preloadAllSubcategories() {
    console.log('Preloading all subcategories...');
    
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
            console.log('Received categories with subcategories:', categories);
            
            // Initialize global subcategories cache
            window.allSubcategoriesData = {};
            
            // Process each category and store its subcategories
            categories.forEach(category => {
                if (category.subcategories && category.subcategories.length > 0) {
                    window.allSubcategoriesData[category.name] = category.subcategories;
                    console.log(`Cached ${category.subcategories.length} subcategories for ${category.name}`);
                } else {
                    // If no subcategories in the main response, fetch them individually
                    fetchSubcategoriesForCategory(category.name);
                }
            });
            
            console.log('All subcategories preloaded successfully');
            
            // Auto-load subcategories for the first category if available
            const firstCategoryCard = document.querySelector('.category-card');
            if (firstCategoryCard) {
                const firstCategoryName = firstCategoryCard.getAttribute('data-category-name') || 
                                          firstCategoryCard.querySelector('.category-name')?.textContent?.trim();
                if (firstCategoryName && window.allSubcategoriesData[firstCategoryName]) {
                    firstCategoryCard.classList.add('active');
                    showSubcategoriesForCategory(firstCategoryName);
                }
            }
        })
        .catch(error => {
            console.error('Error preloading subcategories:', error);
            // Fallback: use individual loading for each category
            initializeFallbackLoading();
        });
}

// Helper function to fetch subcategories for individual categories
function fetchSubcategoriesForCategory(categoryName) {
    fetch(`/api/subcategories/category/${encodeURIComponent(categoryName)}`)
        .then(response => response.ok ? response.json() : [])
        .then(subcategories => {
            if (subcategories && subcategories.length > 0) {
                if (!window.allSubcategoriesData) {
                    window.allSubcategoriesData = {};
                }
                window.allSubcategoriesData[categoryName] = subcategories;
                console.log(`Individually cached ${subcategories.length} subcategories for ${categoryName}`);
            }
        })
        .catch(error => console.error(`Error fetching subcategories for ${categoryName}:`, error));
}

// Fallback initialization if preloading fails
function initializeFallbackLoading() {
    console.log('Using fallback individual loading approach');
    const categoryCards = document.querySelectorAll('.category-card');
    categoryCards.forEach(card => {
        const categoryName = card.getAttribute('data-category-name') || 
                            card.querySelector('.category-name')?.textContent?.trim();
        if (categoryName) {
            fetchSubcategoriesForCategory(categoryName);
        }
    });
}

// Function to display subcategories in the carousel
function displaySubcategories(categoryName, subcategories) {
    console.log('displaySubcategories called with:', categoryName, subcategories);
    const categoryNameElement = document.getElementById("selectedCategoryName");
    if (categoryNameElement) {
        categoryNameElement.textContent = categoryName;
    }
    
    const subcategoriesCarousel = document.getElementById("subcategoriesCarousel");
    if (subcategoriesCarousel) {
        subcategoriesCarousel.style.display = "block";
        console.log('Subcategories carousel shown');
    }
    
    const subcategoryTrack = document.getElementById("subcategoryCarouselTrack");
    if (subcategoryTrack) {
        subcategoryTrack.innerHTML = "";
        console.log('Subcategory track cleared');
        
        subcategories.forEach((subcategory, index) => {
            console.log(`Creating card for subcategory ${index}:`, subcategory);
            const subcategoryCard = createSubcategoryCard(subcategory);
            subcategoryTrack.appendChild(subcategoryCard);
        });
        
        // Reset carousel position
        subcategoryTrack.style.transform = "translateX(0px)";
        updateNavigationButtons();
        console.log('Subcategory cards created and added to track');
    }
}

// Function to show subcategories for a selected category (for dynamically loaded categories - backwards compatibility)
function showSubcategoriesForCategoryJS(categoryName, subcategories) {
    if (!subcategories || subcategories.length === 0) {
        const subcategoriesCarousel = document.getElementById("subcategoriesCarousel");
        if (subcategoriesCarousel) {
            subcategoriesCarousel.style.display = "none";
        }
        return;
    }
    
    displaySubcategories(categoryName, subcategories);
}

// Function to create subcategory card elements
function createSubcategoryCard(subcategory) {
    const card = document.createElement("div");
    card.className = "subcategory-card";
    card.setAttribute("data-subcategory-id", subcategory.id);
    card.setAttribute("data-subcategory-name", subcategory.name);
    
    const imageUrl = subcategory.imageUrl || "https://via.placeholder.com/140x120?text=" + encodeURIComponent(subcategory.name);
    
    card.innerHTML = `
        <div class="subcategory-image">
            <img src="${imageUrl}" 
                 alt="${subcategory.name}" 
                 loading="lazy"
                 onerror="this.src='https://via.placeholder.com/140x120?text=${encodeURIComponent(subcategory.name)}'">
        </div>
        <div class="subcategory-content">
            <h6 class="subcategory-name">${subcategory.name}</h6>
            <p class="subcategory-description">${subcategory.description || "Explore products"}</p>
            <div class="subcategory-product-count" data-subcategory-id="${subcategory.id}">
                <span class="loading-dots">Loading...</span>
            </div>
        </div>
    `;
    
    // Add click interaction
    card.addEventListener("click", function() {
        // Visual feedback
        this.style.transform = "scale(0.95)";
        setTimeout(() => {
            this.style.transform = "";
        }, 150);
        
        // Load products for this subcategory
        setTimeout(() => {
            loadProductsForSubcategory(subcategory.id, subcategory.name);
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
                countElement.innerHTML = `<span class="product-count">${productCount} products</span>`;
            }
        })
        .catch(error => {
            console.error('Error loading product count:', error);
            const countElement = document.querySelector(`.subcategory-product-count[data-subcategory-id="${subcategoryId}"]`);
            if (countElement) {
                countElement.innerHTML = '<span class="product-count error">Error loading</span>';
            }
        });
}

// Function to load products for a subcategory (navigate to products page or display modal)
function loadProductsForSubcategory(subcategoryId, subcategoryName) {
    console.log(`Loading products for subcategory: ${subcategoryName} (ID: ${subcategoryId})`);
    
    // Option 1: Navigate to products page with subcategory filter
    window.location.href = `/products?subcategoryId=${subcategoryId}&subcategoryName=${encodeURIComponent(subcategoryName)}`;
    
    // Option 2: Load products in a modal (uncomment if you prefer modal approach)
    /*
    const modal = document.getElementById('productsModal');
    const modalBody = document.getElementById('productsModalBody');
    const modalTitle = document.getElementById('productsModalTitle');
    
    if (modal && modalBody && modalTitle) {
        modalTitle.textContent = `Products in ${subcategoryName}`;
        modalBody.innerHTML = '<div class="loading">Loading products...</div>';
        modal.style.display = 'block';
        
        fetch(`/api/products/subcategory/${subcategoryId}`)
            .then(response => response.json())
            .then(products => {
                displayProductsInModal(products, modalBody);
            })
            .catch(error => {
                console.error('Error loading products:', error);
                modalBody.innerHTML = '<div class="error">Error loading products</div>';
            });
    }
    */
}

// Alternative function for backwards compatibility
function createSubcategoryCardJS(subcategory) {
    return createSubcategoryCard(subcategory);
}

// Function to navigate subcategory carousel
function navigateSubcategoryCarousel(direction) {
    const track = document.getElementById("subcategoryCarouselTrack");
    if (!track) return;
    
    const cards = track.querySelectorAll(".subcategory-card");
    if (cards.length === 0) return;
    
    const cardWidth = cards[0].offsetWidth + 15; // card width + margin
    const containerWidth = track.parentElement.offsetWidth;
    const visibleCards = Math.floor(containerWidth / cardWidth);
    const maxScroll = Math.max(0, (cards.length - visibleCards) * cardWidth);
    
    const currentTransform = track.style.transform;
    const currentScroll = currentTransform ? parseInt(currentTransform.match(/-?\d+/)?.[0] || '0') : 0;
    
    let newScroll;
    if (direction === 'prev') {
        newScroll = Math.min(0, currentScroll + cardWidth);
    } else {
        newScroll = Math.max(-maxScroll, currentScroll - cardWidth);
    }
    
    track.style.transform = `translateX(${newScroll}px)`;
    updateNavigationButtons();
}

// Function to update navigation button states
function updateNavigationButtons() {
    const track = document.getElementById("subcategoryCarouselTrack");
    if (!track) return;
    
    const cards = track.querySelectorAll(".subcategory-card");
    if (cards.length === 0) return;
    
    const cardWidth = cards[0].offsetWidth + 15;
    const containerWidth = track.parentElement.offsetWidth;
    const visibleCards = Math.floor(containerWidth / cardWidth);
    const maxScroll = Math.max(0, (cards.length - visibleCards) * cardWidth);
    
    const currentTransform = track.style.transform;
    const currentScroll = Math.abs(currentTransform ? parseInt(currentTransform.match(/-?\d+/)?.[0] || '0') : 0);
    
    const prevBtn = document.querySelector('.subcategory-prev');
    const nextBtn = document.querySelector('.subcategory-next');
    
    if (prevBtn) {
        prevBtn.disabled = currentScroll === 0;
        prevBtn.style.opacity = currentScroll === 0 ? '0.5' : '1';
    }
    
    if (nextBtn) {
        nextBtn.disabled = currentScroll >= maxScroll;
        nextBtn.style.opacity = currentScroll >= maxScroll ? '0.5' : '1';
    }
}

// Function to initialize category click handlers for immediate loading
function initializeCategoryHandlers() {
    const categoryCards = document.querySelectorAll('.category-card');
    categoryCards.forEach(card => {
        const categoryName = card.getAttribute('data-category-name') || 
                            card.querySelector('.category-name')?.textContent?.trim();
        
        if (categoryName) {
            card.addEventListener('click', function() {
                // Remove active class from other categories
                categoryCards.forEach(c => c.classList.remove('active'));
                // Add active class to clicked category
                this.classList.add('active');
                
                // Load subcategories for this category (now with preloaded data)
                showSubcategoriesForCategory(categoryName);
            });
        }
    });
}

// Initialize subcategory carousel when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Add event listeners for navigation buttons
    const prevBtn = document.querySelector('.subcategory-prev');
    const nextBtn = document.querySelector('.subcategory-next');
    
    if (prevBtn) {
        prevBtn.addEventListener('click', () => navigateSubcategoryCarousel('prev'));
    }
    
    if (nextBtn) {
        nextBtn.addEventListener('click', () => navigateSubcategoryCarousel('next'));
    }
    
    // Initialize category click handlers for immediate loading
    initializeCategoryHandlers();
    
    // Handle window resize to update navigation buttons
    window.addEventListener('resize', updateNavigationButtons);
    
    // Preload all subcategories for all categories immediately
    setTimeout(() => {
        preloadAllSubcategories();
    }, 100); // Small delay to ensure DOM is fully ready
});

// Utility function to show loading state
function showLoadingState(element) {
    if (element) {
        element.classList.add('loading');
        element.innerHTML = '<div class="loading-spinner"></div><span>Loading...</span>';
    }
}

// Utility function to hide loading state
function hideLoadingState(element) {
    if (element) {
        element.classList.remove('loading');
    }
}
