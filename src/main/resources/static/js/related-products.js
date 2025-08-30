// Related Products functionality
class RelatedProductsManager {
    constructor() {
        this.gridContainer = document.getElementById('related-products-grid');
        this.loadingMessage = document.getElementById('loading-message');
        this.noProductsMessage = document.getElementById('no-products-message');
    }

    // Main function to load related products
    async loadRelatedProducts(currentProductId) {
        try {
            this.showLoading();
            
            console.log('Loading related products for product ID:', currentProductId);
            
            const response = await fetch(`/products/api/related/${currentProductId}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const relatedProducts = await response.json();
            console.log('Found related products:', relatedProducts.length);
            
            this.displayProductCards(relatedProducts);
            
        } catch (error) {
            console.error('Error loading related products:', error);
            this.showError('Failed to load related products. Please try again.');
        }
    }

    // Display products as Bootstrap cards
    displayProductCards(products) {
        this.hideLoading();
        
        if (!products || products.length === 0) {
            this.showNoProducts();
            return;
        }

        this.gridContainer.innerHTML = '';
        
        products.forEach(product => {
            const cardHtml = this.createProductCard(product);
            this.gridContainer.appendChild(cardHtml);
        });

        // Show the related products section
        document.getElementById('related-products-section').style.display = 'block';
    }

    // Create individual product card
    createProductCard(product) {
        const cardDiv = document.createElement('div');
        cardDiv.className = 'col-lg-3 col-md-4 col-sm-6 mb-4';
        
        const discountBadge = product.discountedPrice ? 
            `<div class="badge bg-danger position-absolute top-0 end-0 m-2">
                ${this.calculateDiscount(product.price, product.discountedPrice)}% OFF
             </div>` : '';
        
        const priceSection = product.discountedPrice ?
            `<div class="price-section">
                <span class="text-danger fw-bold fs-5">$${product.discountedPrice}</span>
                <small class="text-muted text-decoration-line-through ms-2">$${product.price}</small>
             </div>` :
            `<div class="price-section">
                <span class="text-primary fw-bold fs-5">$${product.price}</span>
             </div>`;

        const stockStatus = product.quantity > 0 ? 
            `<small class="text-success"><i class="fas fa-check-circle"></i> In Stock (${product.quantity})</small>` :
            `<small class="text-danger"><i class="fas fa-times-circle"></i> Out of Stock</small>`;

        cardDiv.innerHTML = `
            <div class="card h-100 shadow-sm product-card" data-product-id="${product.id}">
                <div class="position-relative">
                    <img src="${product.imageUrl || '/images/default-product.jpg'}" 
                         class="card-img-top" 
                         alt="${product.name}" 
                         style="height: 200px; object-fit: cover;"
                         onerror="this.src='/images/default-product.jpg'">
                    ${discountBadge}
                </div>
                
                <div class="card-body d-flex flex-column">
                    <h6 class="card-title text-truncate" title="${product.name}">${product.name}</h6>
                    
                    <p class="card-text text-muted small flex-grow-1">
                        ${product.description ? this.truncateText(product.description, 80) : 'No description available'}
                    </p>
                    
                    <div class="product-details mb-2">
                        ${product.brand ? `<small class="text-muted d-block"><i class="fas fa-tag"></i> Brand: ${product.brand}</small>` : ''}
                        ${product.unit ? `<small class="text-muted d-block"><i class="fas fa-balance-scale"></i> Unit: ${product.unit}</small>` : ''}
                        ${stockStatus}
                    </div>
                    
                    ${priceSection}
                </div>
                
                <div class="card-footer bg-transparent border-top-0 pt-0">
                    <div class="d-grid gap-2">
                        <a href="/products/${product.id}" class="btn btn-outline-primary btn-sm">
                            <i class="fas fa-eye"></i> View Details
                        </a>
                        ${product.quantity > 0 ? 
                            `<button onclick="addToCart('${product.id}', 1)" class="btn btn-success btn-sm">
                                <i class="fas fa-cart-plus"></i> Add to Cart
                             </button>` :
                            `<button class="btn btn-secondary btn-sm" disabled>
                                <i class="fas fa-ban"></i> Out of Stock
                             </button>`
                        }
                    </div>
                </div>
            </div>
        `;
        
        return cardDiv;
    }

    // Helper functions
    truncateText(text, length) {
        return text.length > length ? text.substring(0, length) + '...' : text;
    }

    calculateDiscount(originalPrice, discountedPrice) {
        return Math.round(((originalPrice - discountedPrice) / originalPrice) * 100);
    }

    showLoading() {
        if (this.loadingMessage) {
            this.loadingMessage.style.display = 'block';
        }
        if (this.gridContainer) {
            this.gridContainer.innerHTML = '';
        }
    }

    hideLoading() {
        if (this.loadingMessage) {
            this.loadingMessage.style.display = 'none';
        }
    }

    showNoProducts() {
        if (this.noProductsMessage) {
            this.noProductsMessage.style.display = 'block';
        }
        if (this.gridContainer) {
            this.gridContainer.innerHTML = '';
        }
    }

    showError(message) {
        this.hideLoading();
        this.gridContainer.innerHTML = `
            <div class="col-12">
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-triangle"></i> ${message}
                </div>
            </div>
        `;
    }
}

// Global function to add product to cart
function addToCart(productId, quantity = 1) {
    // Create a form and submit it to add the product to cart
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/cart/add-product';
    
    const productIdInput = document.createElement('input');
    productIdInput.type = 'hidden';
    productIdInput.name = 'id';
    productIdInput.value = productId;
    
    const quantityInput = document.createElement('input');
    quantityInput.type = 'hidden';
    quantityInput.name = 'quantity';
    quantityInput.value = quantity;
    
    form.appendChild(productIdInput);
    form.appendChild(quantityInput);
    document.body.appendChild(form);
    form.submit();
}

// Extract product ID from current URL
function getCurrentProductId() {
    const path = window.location.pathname;
    const matches = path.match(/\/products\/([^\/]+)$/);
    return matches ? matches[1] : null;
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    const currentProductId = getCurrentProductId();
    
    if (currentProductId) {
        const relatedProductsManager = new RelatedProductsManager();
        relatedProductsManager.loadRelatedProducts(currentProductId);
    }
});