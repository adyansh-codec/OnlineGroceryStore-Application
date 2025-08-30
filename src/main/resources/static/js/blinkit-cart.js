/**
 * Blinkit-Style Shopping Cart System
 * Implements cart functionality similar to blinkit.com
 */

class BlinkitCart {
    constructor() {
        this.cartKey = 'blinkit_grocery_cart';
        this.cart = this.loadCart();
        this.listeners = [];
        this.isDrawerOpen = false;
        this.init();
    }

    init() {
        this.createCartUI();
        this.loadCartFromServer();
        this.updateCartBadge();
    }

    // Load cart from localStorage
    loadCart() {
        try {
            const saved = localStorage.getItem(this.cartKey);
            return saved ? JSON.parse(saved) : {};
        } catch (error) {
            console.error('Error loading cart:', error);
            return {};
        }
    }

    // Save cart to localStorage
    saveCart() {
        try {
            localStorage.setItem(this.cartKey, JSON.stringify(this.cart));
            this.notifyListeners();
        } catch (error) {
            console.error('Error saving cart:', error);
        }
    }

    // Add item to cart (Blinkit style)
    async addItem(productId, productName, price, imageUrl, unit = '1 unit') {
        if (!productId) return false;

        const numericPrice = parseFloat(price) || 0;
        
        if (this.cart[productId]) {
            this.cart[productId].quantity += 1;
        } else {
            this.cart[productId] = {
                id: productId,
                name: productName || 'Product',
                price: numericPrice,
                imageUrl: imageUrl || '',
                unit: unit,
                quantity: 1,
                addedAt: Date.now()
            };
        }

        this.saveCart();
        this.updateCartBadge();
        this.updateProductButtons(productId);
        this.showAddAnimation(productId);
        
        // Sync with server
        try {
            await this.syncWithServer('add', productId, 1);
        } catch (error) {
            console.warn('Server sync failed:', error);
        }

        return true;
    }

    // Remove item from cart
    async removeItem(productId) {
        if (!this.cart[productId]) return false;

        if (this.cart[productId].quantity > 1) {
            this.cart[productId].quantity -= 1;
        } else {
            delete this.cart[productId];
        }

        this.saveCart();
        this.updateCartBadge();
        this.updateProductButtons(productId);
        this.updateCartDrawer();

        // Sync with server
        try {
            await this.syncWithServer('remove', productId, 1);
        } catch (error) {
            console.warn('Server sync failed:', error);
        }

        return true;
    }

    // Get item quantity
    getQuantity(productId) {
        return this.cart[productId] ? this.cart[productId].quantity : 0;
    }

    // Get total items count
    getTotalItems() {
        return Object.values(this.cart).reduce((total, item) => total + item.quantity, 0);
    }

    // Get total price
    getTotalPrice() {
        return Object.values(this.cart).reduce((total, item) => total + (item.price * item.quantity), 0);
    }

    // Create Blinkit-style cart UI
    createCartUI() {
        // Remove existing cart elements
        const existing = document.getElementById('blinkit-cart-container');
        if (existing) existing.remove();

        // Create cart container
        const cartContainer = document.createElement('div');
        cartContainer.id = 'blinkit-cart-container';
        cartContainer.innerHTML = `
            <!-- Floating Cart Button -->
            <div id="cart-float-button" class="blinkit-cart-float">
                <div class="cart-icon">
                    <i class="fas fa-shopping-bag"></i>
                    <span id="cart-badge" class="cart-badge">0</span>
                </div>
                <div class="cart-text">
                    <div class="cart-items-count">0 items</div>
                    <div class="cart-total">₹0</div>
                </div>
            </div>

            <!-- Cart Drawer -->
            <div id="cart-drawer" class="blinkit-cart-drawer">
                <div class="cart-drawer-overlay" onclick="blinkitCart.closeDrawer()"></div>
                <div class="cart-drawer-content">
                    <div class="cart-drawer-header">
                        <h3>My Basket</h3>
                        <button onclick="blinkitCart.closeDrawer()" class="close-btn">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <div class="cart-drawer-body" id="cart-items-container">
                        <!-- Cart items will be populated here -->
                    </div>
                    <div class="cart-drawer-footer">
                        <div class="cart-total-section">
                            <div class="total-items">0 items</div>
                            <div class="total-price">₹0</div>
                        </div>
                        <button class="checkout-btn" onclick="blinkitCart.checkout()">
                            Proceed to Checkout
                        </button>
                    </div>
                </div>
            </div>

            <!-- Toast Notifications -->
            <div id="blinkit-toast-container" class="toast-container"></div>
        `;

        document.body.appendChild(cartContainer);

        // Add event listeners
        document.getElementById('cart-float-button').addEventListener('click', () => {
            this.openDrawer();
        });

        // Add CSS styles
        this.addCartStyles();
    }

    // Add Blinkit-style CSS
    addCartStyles() {
        const styleId = 'blinkit-cart-styles';
        if (document.getElementById(styleId)) return;

        const style = document.createElement('style');
        style.id = styleId;
        style.textContent = `
            /* Blinkit Cart Styles */
            .blinkit-cart-float {
                position: fixed;
                bottom: 20px;
                left: 20px;
                background: #0c831f;
                color: white;
                padding: 12px 16px;
                border-radius: 12px;
                cursor: pointer;
                box-shadow: 0 4px 20px rgba(12, 131, 31, 0.3);
                z-index: 1000;
                display: flex;
                align-items: center;
                gap: 12px;
                transition: all 0.3s ease;
                min-width: 120px;
            }

            .blinkit-cart-float:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 25px rgba(12, 131, 31, 0.4);
            }

            .cart-icon {
                position: relative;
                font-size: 20px;
            }

            .cart-badge {
                position: absolute;
                top: -8px;
                right: -8px;
                background: #ff4444;
                color: white;
                border-radius: 10px;
                padding: 2px 6px;
                font-size: 11px;
                font-weight: bold;
                min-width: 18px;
                text-align: center;
                line-height: 1.2;
            }

            .cart-text {
                display: flex;
                flex-direction: column;
                align-items: flex-start;
            }

            .cart-items-count {
                font-size: 12px;
                opacity: 0.9;
                line-height: 1;
            }

            .cart-total {
                font-size: 14px;
                font-weight: bold;
                line-height: 1;
            }

            /* Cart Drawer */
            .blinkit-cart-drawer {
                position: fixed;
                top: 0;
                right: 0;
                width: 100%;
                height: 100%;
                z-index: 9999;
                pointer-events: none;
                opacity: 0;
                transition: opacity 0.3s ease;
            }

            .blinkit-cart-drawer.open {
                opacity: 1;
                pointer-events: all;
            }

            .cart-drawer-overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
            }

            .cart-drawer-content {
                position: absolute;
                top: 0;
                right: 0;
                width: 400px;
                max-width: 90vw;
                height: 100%;
                background: white;
                transform: translateX(100%);
                transition: transform 0.3s ease;
                display: flex;
                flex-direction: column;
            }

            .blinkit-cart-drawer.open .cart-drawer-content {
                transform: translateX(0);
            }

            .cart-drawer-header {
                padding: 20px;
                border-bottom: 1px solid #eee;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .cart-drawer-header h3 {
                margin: 0;
                font-size: 20px;
                color: #333;
            }

            .close-btn {
                background: none;
                border: none;
                font-size: 20px;
                color: #666;
                cursor: pointer;
                padding: 5px;
            }

            .cart-drawer-body {
                flex: 1;
                overflow-y: auto;
                padding: 20px;
            }

            .cart-item {
                display: flex;
                align-items: center;
                gap: 12px;
                padding: 12px 0;
                border-bottom: 1px solid #f0f0f0;
            }

            .cart-item-image {
                width: 50px;
                height: 50px;
                object-fit: cover;
                border-radius: 8px;
                background: #f8f9fa;
            }

            .cart-item-details {
                flex: 1;
            }

            .cart-item-name {
                font-size: 14px;
                font-weight: 500;
                color: #333;
                margin-bottom: 4px;
            }

            .cart-item-unit {
                font-size: 12px;
                color: #666;
            }

            .cart-item-price {
                font-size: 14px;
                font-weight: 600;
                color: #0c831f;
            }

            .cart-drawer-footer {
                padding: 20px;
                border-top: 1px solid #eee;
                background: #fafafa;
            }

            .cart-total-section {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 16px;
            }

            .total-items {
                font-size: 14px;
                color: #666;
            }

            .total-price {
                font-size: 18px;
                font-weight: bold;
                color: #333;
            }

            .checkout-btn {
                width: 100%;
                background: #0c831f;
                color: white;
                border: none;
                padding: 14px;
                border-radius: 12px;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: background 0.3s ease;
            }

            .checkout-btn:hover {
                background: #0a6f1a;
            }

            /* Product Buttons */
            .blinkit-add-btn {
                background: #0c831f;
                color: white;
                border: none;
                padding: 8px 16px;
                border-radius: 8px;
                font-size: 12px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .blinkit-add-btn:hover {
                background: #0a6f1a;
                transform: translateY(-1px);
            }

            .blinkit-quantity-controls {
                display: flex;
                align-items: center;
                gap: 8px;
                background: #0c831f;
                border-radius: 8px;
                padding: 4px;
            }

            .quantity-btn {
                background: none;
                border: none;
                color: white;
                width: 28px;
                height: 28px;
                border-radius: 6px;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 16px;
                transition: background 0.2s ease;
            }

            .quantity-btn:hover {
                background: rgba(255, 255, 255, 0.2);
            }

            .quantity-display {
                color: white;
                font-weight: 600;
                min-width: 24px;
                text-align: center;
                font-size: 14px;
            }

            /* Toast Notifications */
            .toast-container {
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 10000;
            }

            .blinkit-toast {
                background: #0c831f;
                color: white;
                padding: 12px 16px;
                border-radius: 8px;
                margin-bottom: 8px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                transform: translateX(100%);
                opacity: 0;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                gap: 8px;
            }

            .blinkit-toast.show {
                transform: translateX(0);
                opacity: 1;
            }

            /* Empty cart state */
            .empty-cart {
                text-align: center;
                padding: 40px 20px;
                color: #666;
            }

            .empty-cart i {
                font-size: 48px;
                color: #ddd;
                margin-bottom: 16px;
            }

            .empty-cart h4 {
                margin-bottom: 8px;
                color: #333;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .cart-drawer-content {
                    width: 100%;
                    max-width: 100vw;
                }
                
                .blinkit-cart-float {
                    bottom: 80px;
                }
            }
        `;
        document.head.appendChild(style);
    }

    // Update cart badge and float button
    updateCartBadge() {
        const badge = document.getElementById('cart-badge');
        const itemsCount = document.querySelector('.cart-items-count');
        const totalPrice = document.querySelector('.cart-total');
        const floatButton = document.getElementById('cart-float-button');

        const totalItems = this.getTotalItems();
        const total = this.getTotalPrice();

        if (badge) {
            badge.textContent = totalItems;
            badge.style.display = totalItems > 0 ? 'block' : 'none';
        }

        if (itemsCount) {
            itemsCount.textContent = totalItems === 1 ? '1 item' : `${totalItems} items`;
        }

        if (totalPrice) {
            totalPrice.textContent = `₹${total.toFixed(0)}`;
        }

        if (floatButton) {
            floatButton.style.display = totalItems > 0 ? 'flex' : 'none';
        }

        // Update drawer totals
        const drawerItemsCount = document.querySelector('.cart-drawer-footer .total-items');
        const drawerTotalPrice = document.querySelector('.cart-drawer-footer .total-price');
        
        if (drawerItemsCount) {
            drawerItemsCount.textContent = totalItems === 1 ? '1 item' : `${totalItems} items`;
        }
        
        if (drawerTotalPrice) {
            drawerTotalPrice.textContent = `₹${total.toFixed(0)}`;
        }
    }

    // Update product buttons based on cart state
    updateProductButtons(productId = null) {
        const buttons = productId ? 
            document.querySelectorAll(`[data-product-id="${productId}"]`) :
            document.querySelectorAll('[data-product-id]');

        buttons.forEach(button => {
            const pid = button.getAttribute('data-product-id');
            const quantity = this.getQuantity(pid);
            const container = button.closest('.product-actions') || button.parentElement;

            if (quantity > 0) {
                // Show quantity controls
                container.innerHTML = `
                    <div class="blinkit-quantity-controls" data-product-id="${pid}">
                        <button class="quantity-btn" onclick="blinkitCart.removeItem('${pid}')">
                            <i class="fas fa-minus"></i>
                        </button>
                        <span class="quantity-display">${quantity}</span>
                        <button class="quantity-btn" onclick="blinkitCart.addToCart('${pid}')">
                            <i class="fas fa-plus"></i>
                        </button>
                    </div>
                `;
            } else {
                // Show add button
                const productName = button.getAttribute('data-product-name') || 'Product';
                const price = button.getAttribute('data-price') || '0';
                const imageUrl = button.getAttribute('data-image') || '';
                const unit = button.getAttribute('data-unit') || '1 unit';
                
                container.innerHTML = `
                    <button class="blinkit-add-btn" data-product-id="${pid}" 
                            data-product-name="${productName}" data-price="${price}" 
                            data-image="${imageUrl}" data-unit="${unit}"
                            onclick="blinkitCart.addToCart('${pid}')">
                        Add
                    </button>
                `;
            }
        });
    }

    // Open cart drawer
    openDrawer() {
        const drawer = document.getElementById('cart-drawer');
        if (drawer) {
            this.updateCartDrawer();
            drawer.classList.add('open');
            this.isDrawerOpen = true;
            document.body.style.overflow = 'hidden';
        }
    }

    // Close cart drawer
    closeDrawer() {
        const drawer = document.getElementById('cart-drawer');
        if (drawer) {
            drawer.classList.remove('open');
            this.isDrawerOpen = false;
            document.body.style.overflow = '';
        }
    }

    // Update cart drawer content
    updateCartDrawer() {
        const container = document.getElementById('cart-items-container');
        if (!container) return;

        const cartItems = Object.values(this.cart);

        if (cartItems.length === 0) {
            container.innerHTML = `
                <div class="empty-cart">
                    <i class="fas fa-shopping-bag"></i>
                    <h4>Your basket is empty</h4>
                    <p>Add products to get started</p>
                </div>
            `;
            return;
        }

        container.innerHTML = cartItems.map(item => `
            <div class="cart-item">
                <img src="${item.imageUrl || '/images/default-product.png'}" 
                     alt="${item.name}" class="cart-item-image"
                     onerror="this.src='/images/default-product.png'">
                <div class="cart-item-details">
                    <div class="cart-item-name">${item.name}</div>
                    <div class="cart-item-unit">${item.unit}</div>
                </div>
                <div class="cart-item-price">₹${(item.price * item.quantity).toFixed(0)}</div>
                <div class="blinkit-quantity-controls">
                    <button class="quantity-btn" onclick="blinkitCart.removeItem('${item.id}')">
                        <i class="fas fa-minus"></i>
                    </button>
                    <span class="quantity-display">${item.quantity}</span>
                    <button class="quantity-btn" onclick="blinkitCart.addItem('${item.id}', '${item.name}', ${item.price}, '${item.imageUrl}', '${item.unit}')">
                        <i class="fas fa-plus"></i>
                    </button>
                </div>
            </div>
        `).join('');
    }

    // Add item with animation
    async addToCart(productId) {
        const button = document.querySelector(`[data-product-id="${productId}"]`);
        if (!button) return;

        const productName = button.getAttribute('data-product-name');
        const price = button.getAttribute('data-price');
        const imageUrl = button.getAttribute('data-image');
        const unit = button.getAttribute('data-unit');

        await this.addItem(productId, productName, price, imageUrl, unit);
    }

    // Show add animation
    showAddAnimation(productId) {
        this.showToast(`Added to basket`, 'success');
    }

    // Show toast notification
    showToast(message, type = 'success') {
        const container = document.getElementById('blinkit-toast-container');
        if (!container) return;

        const toast = document.createElement('div');
        toast.className = 'blinkit-toast';
        toast.innerHTML = `
            <i class="fas fa-${type === 'success' ? 'check' : 'info'}"></i>
            <span>${message}</span>
        `;

        container.appendChild(toast);

        // Animate in
        setTimeout(() => toast.classList.add('show'), 100);

        // Remove after 3 seconds
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }

    // Sync with server
    async syncWithServer(action, productId, quantity) {
        try {
            const formData = new FormData();
            formData.append('id', productId);
            formData.append('quantity', quantity);

            const url = action === 'add' ? '/cart/add-product' : '/cart/remove-product';
            const method = action === 'add' ? 'POST' : 'DELETE';

            const response = await fetch(url, {
                method: method,
                body: method === 'POST' ? formData : new URLSearchParams({id: productId}),
                headers: method === 'DELETE' ? {'Content-Type': 'application/x-www-form-urlencoded'} : {}
            });

            return response.ok;
        } catch (error) {
            console.error('Server sync failed:', error);
            return false;
        }
    }

    // Load cart from server
    async loadCartFromServer() {
        try {
            const response = await fetch('/cart/api/items');
            if (response.ok) {
                const data = await response.json();
                // Merge server cart with local cart if needed
                console.log('Server cart loaded:', data);
            }
        } catch (error) {
            console.log('Server cart not available, using local storage');
        }
    }

    // Checkout
    checkout() {
        if (this.getTotalItems() === 0) {
            this.showToast('Your basket is empty', 'info');
            return;
        }

        // Redirect to checkout or show login modal
        window.location.href = '/cart/details';
    }

    // Add listeners
    addListener(callback) {
        this.listeners.push(callback);
    }

    // Notify listeners
    notifyListeners() {
        this.listeners.forEach(callback => {
            try {
                callback(this.cart, this.getTotalItems(), this.getTotalPrice());
            } catch (error) {
                console.error('Cart listener error:', error);
            }
        });
    }
}

// Initialize cart when DOM loads
let blinkitCart;

document.addEventListener('DOMContentLoaded', function() {
    blinkitCart = new BlinkitCart();
    
    // Update product buttons after page load
    setTimeout(() => {
        blinkitCart.updateProductButtons();
    }, 500);
});

// Global helper functions
function addToBlinkitCart(productId, productName, price, imageUrl, unit) {
    if (blinkitCart) {
        return blinkitCart.addItem(productId, productName, price, imageUrl, unit);
    }
    return false;
}

function removeFromBlinkitCart(productId) {
    if (blinkitCart) {
        return blinkitCart.removeItem(productId);
    }
    return false;
}