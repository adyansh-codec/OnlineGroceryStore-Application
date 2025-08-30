// Simple Related Products Script
document.addEventListener('DOMContentLoaded', function() {
    console.log('Related products script loaded');
    
    // Extract product ID from URL (e.g., /products/prod-21)
    const path = window.location.pathname;
    const productIdMatch = path.match(/\/products\/(.+)$/);
    
    if (!productIdMatch) {
        console.log('No product ID found in URL');
        return;
    }
    
    const productId = productIdMatch[1];
    console.log('Current product ID:', productId);
    
    // Load related products
    loadRelatedProducts(productId);
});

function loadRelatedProducts(productId) {
    console.log('Loading related products for:', productId);
    
    const apiUrl = `/products/api/related/${productId}`;
    console.log('Fetching from:', apiUrl);
    
    fetch(apiUrl)
        .then(response => {
            console.log('API response status:', response.status);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(products => {
            console.log('Related products received:', products.length);
            console.log('Products data:', products);
            displayRelatedProducts(products);
        })
        .catch(error => {
            console.error('Error loading related products:', error);
            showError('Failed to load related products');
        });
}

function displayRelatedProducts(products) {
    const container = document.getElementById('related-products-container');
    
    if (!container) {
        console.error('Related products container not found!');
        return;
    }
    
    if (!products || products.length === 0) {
        container.innerHTML = `
            <div class="col-12">
                <div class="alert alert-info text-center">
                    <i class="fas fa-info-circle"></i> No related products found for this category.
                </div>
            </div>
        `;
        return;
    }
    
    // Clear existing content
    container.innerHTML = '';
    
    // Create product cards
    products.forEach(product => {
        const card = createProductCard(product);
        container.appendChild(card);
    });
    
    // Show the related products section
    const section = document.getElementById('related-products-section');
    if (section) {
        section.style.display = 'block';
    }
    
    console.log(`Displayed ${products.length} related products`);
}

function createProductCard(product) {
    const cardDiv = document.createElement('div');
    cardDiv.className = 'col-lg-3 col-md-4 col-sm-6 mb-4';
    
    const discountBadge = product.discountedPrice ? 
        `<span class="badge bg-danger position-absolute top-0 end-0 m-2">SALE</span>` : '';
    
    const price = product.discountedPrice ? 
        `<div class="mb-2">
            <span class="text-danger fw-bold">$${product.discountedPrice}</span>
            <small class="text-muted text-decoration-line-through ms-2">$${product.price}</small>
         </div>` :
        `<div class="mb-2">
            <span class="text-primary fw-bold">$${product.price}</span>
         </div>`;
    
    const stockInfo = product.quantity > 0 ? 
        `<small class="text-success">✓ In Stock (${product.quantity})</small>` :
        `<small class="text-danger">✗ Out of Stock</small>`;
    
    cardDiv.innerHTML = `
        <div class="card h-100 shadow-sm">
            <div class="position-relative">
                <img src="${product.imageUrl || '/images/default-product.jpg'}" 
                     class="card-img-top" 
                     alt="${product.name}" 
                     style="height: 200px; object-fit: cover;"
                     onerror="this.src='https://via.placeholder.com/200x200?text=No+Image'">
                ${discountBadge}
            </div>
            
            <div class="card-body d-flex flex-column">
                <h6 class="card-title">${product.name}</h6>
                <p class="card-text text-muted small flex-grow-1">
                    ${product.description ? (product.description.length > 60 ? product.description.substring(0, 60) + '...' : product.description) : ''}
                </p>
                
                ${product.brand ? `<small class="text-muted">Brand: ${product.brand}</small>` : ''}
                ${product.unit ? `<small class="text-muted">Unit: ${product.unit}</small>` : ''}
                <div class="mt-2">${stockInfo}</div>
                
                ${price}
            </div>
            
            <div class="card-footer bg-light">
                <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                    <a href="/products/${product.id}" class="btn btn-outline-primary btn-sm">
                        View Details
                    </a>
                    ${product.quantity > 0 ? 
                        `<button onclick="addToCart('${product.id}')" class="btn btn-success btn-sm">
                            Add to Cart
                         </button>` :
                        `<button class="btn btn-secondary btn-sm" disabled>
                            Out of Stock
                         </button>`
                    }
                </div>
            </div>
        </div>
    `;
    
    return cardDiv;
}

function showError(message) {
    const container = document.getElementById('related-products-container');
    if (container) {
        container.innerHTML = `
            <div class="col-12">
                <div class="alert alert-danger text-center">
                    <i class="fas fa-exclamation-triangle"></i> ${message}
                </div>
            </div>
        `;
    }
}

// Add to cart function
function addToCart(productId) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/cart/add-product';
    
    const idInput = document.createElement('input');
    idInput.type = 'hidden';
    idInput.name = 'id';
    idInput.value = productId;
    
    const quantityInput = document.createElement('input');
    quantityInput.type = 'hidden';
    quantityInput.name = 'quantity';
    quantityInput.value = '1';
    
    form.appendChild(idInput);
    form.appendChild(quantityInput);
    document.body.appendChild(form);
    form.submit();
}