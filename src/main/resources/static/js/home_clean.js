(() => {
    function formatProduct(product) {
        if (product.discountedPrice == null) {
            return '<div class="product col-md-2 mt-2 bg-product rounded ml-4 mr-4 mb-5">'
                + '<div class="text-center mt-3">'
                + `<a href="/products/details/${product.id}"><img src="${product.imageUrl}" 
class="product-image-home img-thumbnail px-auto" alt="Image not loaded..."/></a>`
                + '</div>'
                + `<h5 class="text-center font-weight-bold mt-3">Name: ${product.name}</h5>`
                + `<h5 class="text-center font-weight-bold" style="color: black">Price: â‚¹${product.price.toFixed(2)}</h5>`
                + '</div>'
        } else {
            return '<div class="product col-md-2 mt-2 bg-product rounded ml-4 mr-4 mb-5">'
                + '<div class="text-center mt-3">'
                + `<a href="/products/details/${product.id}"><img src="${product.imageUrl}" 
class="product-image-home img-thumbnail px-auto" alt="Image not loaded..."/></a>`
                + '</div>'
                + `<h5 class="text-center font-weight-bold mt-3">Name: ${product.name}</h5>`
                + `<h5 class="text-center font-weight-bold" style="color: black">Price: <del>â‚¹${product.price.toFixed(2)}</del></h5>`
                + `<h5 class="text-center font-weight-bold" style="color: orangered">New Price: â‚¹${product.discountedPrice.toFixed(2)}</h5>`
                + '</div>'
        }
    }
    fetch('/products/fetch')
        .then((response) => response.json())
        .then((json) => {
            $('.products-data').empty();
            if (json.length === 0) {
                $('.products-data').append(`<h1 class="text-center font-weight-bold">There are no products in the ${category} category.</h1>`)
            } else {
                for (let i = 0; i < json.length; i++) {
                    $('.products-data').append(formatProduct(json[i]));
                }
            }
        })
        .catch((err) => console.log(err));
})();


document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('productSearchInput');
    const productList = document.getElementById('productList');
    const toggleProduct = document.getElementById('toggleProduct');
    const toggleCategory = document.getElementById('toggleCategory');
    let searchMode = 'product'; // default
    let allItems = []; // Store all products or categories

    // Toggle button logic
    toggleProduct.addEventListener('click', function() {
        toggleProduct.classList.add('active');
        toggleCategory.classList.remove('active');
        searchMode = 'product';
        productList.style.display = 'none';
        searchInput.value = '';
        allItems = [];
    });
    toggleCategory.addEventListener('click', function() {
        toggleCategory.classList.add('active');
        toggleProduct.classList.remove('active');
        searchMode = 'category';
        productList.style.display = 'none';
        searchInput.value = '';
        allItems = [];
    });

    // Fetch and display items on focus
    searchInput.addEventListener('focus', function() {
        let url = searchMode === 'product' ? '/products/all/name' : '/categories/all/name';
        fetch(url)
            .then(response => response.json())
            .then(items => {
                allItems = items; // Save all items for filtering
                renderList(allItems, searchInput.value);
            });
    });

    // Filter items as user types
    searchInput.addEventListener('input', function() {
        if (allItems.length > 0) {
            const filter = searchInput.value.toLowerCase();
            const filteredItems = allItems.filter(itemName => itemName.toLowerCase().includes(filter));
            renderList(filteredItems, searchInput.value);
        }
    });

    // Render list helper
    function renderList(items, inputValue) {
        productList.innerHTML = '';
        if (items.length > 0) {
            items.forEach(itemName => {
                const item = document.createElement('button');
                item.type = 'button';
                item.className = 'list-group-item list-group-item-action';
                item.textContent = itemName;
                item.onclick = function() {
                    searchInput.value = itemName;
                    productList.style.display = 'none';
                    
                    // If category mode is selected, fetch and display products for this category
                    if (searchMode === 'category') {
                        fetchProductsByCategory(itemName);
                    }
                };
                productList.appendChild(item);
            });
            productList.style.display = 'block';
        } else {
            productList.style.display = 'none';
        }
    }

    // Function to fetch and display products by category
    function fetchProductsByCategory(categoryName) {
        // Create category mapping similar to the one in category-products.html
        const categoryMapping = {
            'Fruits And Vegetables': ['Fruits', 'Vegetables', 'Fruit', 'Vegetable'],
            'Meat And Fish': ['Meat', 'Fish', 'Meats'],
            'Dairy and chilled': ['Dairy', 'Milk', 'Cheese'],
            'Bread And Tortilas': ['Bakery', 'Bread', 'Tortillas'],
            'Drinks': ['Beverages', 'Drink', 'Juice'],
            'Baby & Child': ['Baby', 'Child', 'Kids'],
            'Cosmetics': ['Personal Care', 'Beauty', 'Cosmetics'],
            'Pharmacy': ['Health', 'Medicine', 'Pharmacy'],
            'Pets': ['Pet', 'Animal'],
            'Packaged': ['Snacks', 'Packaged'],
            'Frozen': ['Frozen'],
            'Meats And Salami': ['Salami', 'Deli']
        };
        
        // Find the best matching database category name
        let bestMatch = categoryName; // default to the original name
        for (const [dbCategory, aliases] of Object.entries(categoryMapping)) {
            if (dbCategory.toLowerCase() === categoryName.toLowerCase() ||
                aliases.some(alias => alias.toLowerCase() === categoryName.toLowerCase() ||
                                     categoryName.toLowerCase().includes(alias.toLowerCase()) ||
                                     alias.toLowerCase().includes(categoryName.toLowerCase()))) {
                bestMatch = dbCategory;
                break;
            }
        }
        
        console.log(`Fetching products for category: ${categoryName} (mapped to: ${bestMatch})`);
        
        fetch(`/products/api/category/${encodeURIComponent(bestMatch)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch products');
                }
                return response.json();
            })
            .then(products => {
                if (products.length === 0) {
                    alert(`No products found in the "${categoryName}" category.`);
                } else {
                    displayProductsInCarousel(products, categoryName);
                }
            })
            .catch(error => {
                console.error('Error fetching products:', error);
                alert('Error fetching products for this category. Please try again.');
            });
    }

    // Function to create and display product cards in carousel
    function displayProductsInCarousel(products, categoryName) {
        const carouselTrack = document.getElementById('flashCarouselTrack');
        if (!carouselTrack) {
            console.error('Carousel track not found');
            return;
        }

        // Clear existing cards
        carouselTrack.innerHTML = '';

        // Create product cards
        products.forEach((product, index) => {
            const card = createProductCard(product, index + 1);
            carouselTrack.appendChild(card);
        });

        // Reinitialize carousel with new cards
        setTimeout(() => {
            initializeProductCarousel();
        }, 100);

        // Scroll to the carousel section
        const carousel = document.getElementById('productCardsCarousel');
        if (carousel) {
            carousel.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }

        // Show a brief notification about the category
        showCategoryNotification(categoryName, products.length);
    }

    // Function to create individual product card
    function createProductCard(product, index) {
        const card = document.createElement('div');
        card.className = 'card mx-2 mb-2';
        card.style.cssText = 'max-width:180px; font-size:0.9rem; min-width:180px; flex-shrink:0;';

        // Handle product image - use a placeholder if no image
        const imageUrl = product.imageUrl || 'https://via.placeholder.com/150x100?text=No+Image';
        
        // Calculate discount percentage if discounted price exists
        let discountInfo = '';
        let priceDisplay = '';
        if (product.discountedPrice && product.discountedPrice < product.price) {
            const discountPercent = Math.round(((product.price - product.discountedPrice) / product.price) * 100);
            discountInfo = `<span class="badge badge-info ml-1">${discountPercent}% off</span>`;
            priceDisplay = `
                <span class="font-weight-bold text-primary">â‚¹${product.discountedPrice.toFixed(2)}</span>
                <span class="text-muted ml-1"><del>â‚¹${product.price.toFixed(2)}</del></span>
                ${discountInfo}
            `;
        } else {
            priceDisplay = `<span class="font-weight-bold text-primary">â‚¹${product.price.toFixed(2)}</span>`;
        }

        card.innerHTML = `
            <img src="${imageUrl}" class="card-img-top" alt="${product.name}" 
                 style="height:80px;object-fit:contain;" 
                 onerror="this.src='https://via.placeholder.com/150x100?text=No+Image'">
            <div class="card-body p-2">
                <h6 class="card-title mb-1" title="${product.name}">${product.name.length > 30 ? product.name.substring(0, 30) + '...' : product.name}</h6>
                <p class="card-text mb-1 text-muted" style="font-size:0.8rem;">${product.description ? (product.description.length > 40 ? product.description.substring(0, 40) + '...' : product.description) : 'No description'}</p>
                <div class="mb-1">
                    ${priceDisplay}
                </div>
                <div class="mb-1 text-success font-weight-bold" style="font-size:0.75rem;">${product.discountedPrice ? 'Special Offer' : 'Available'}</div>
                <a href="/products/details/${product.id}" class="btn btn-outline-primary btn-sm btn-block view-btn" data-item-id="${product.id}">View Details</a>
            </div>
        `;

        return card;
    }

    // Function to show category notification
    function showCategoryNotification(categoryName, productCount) {
        // Create or update notification
        let notification = document.getElementById('categoryNotification');
        if (!notification) {
            notification = document.createElement('div');
            notification.id = 'categoryNotification';
            notification.style.cssText = `
                position: fixed;
                top: 80px;
                right: 20px;
                z-index: 1000;
                max-width: 300px;
            `;
            document.body.appendChild(notification);
        }

        notification.innerHTML = `
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                <strong>${categoryName}</strong> category loaded!<br>
                Found ${productCount} product${productCount > 1 ? 's' : ''}.
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        `;

        // Auto-hide after 4 seconds
        setTimeout(() => {
            const alert = notification.querySelector('.alert');
            if (alert) {
                $(alert).alert('close');
            }
        }, 4000);
    }

    // Hide list when clicking outside
    document.addEventListener('click', function(e) {
        if (!searchInput.contains(e.target) && !productList.contains(e.target)) {
            productList.style.display = 'none';
        }
    });
});

// Carousel initialization function
function initializeProductCarousel() {
  const track = document.getElementById('flashCarouselTrack');
  const carousel = document.getElementById('productCardsCarousel');
  
  if (!track || !carousel) return;
  
  const cards = track.querySelectorAll('.card');
  let cardWidth = cards[0]?.offsetWidth + 16 || 196; // 16px margin
  let currentIndex = 0;

  function getVisibleCards() {
    return Math.floor(carousel.offsetWidth / cardWidth);
  }

  function updateCarousel() {
    const visibleCards = getVisibleCards();
    if (cards.length <= visibleCards) {
      currentIndex = 0;
      track.style.transform = 'translateX(0px)';
      return;
    }
    
    if (currentIndex > cards.length - visibleCards) {
      currentIndex = 0;
    }
    if (currentIndex < 0) {
      currentIndex = cards.length - visibleCards;
      if (currentIndex < 0) currentIndex = 0;
    }
    track.style.transform = `translateX(-${cardWidth * currentIndex}px)`;
  }

  // Update button handlers
  const nextBtn = document.getElementById('carouselNext');
  const prevBtn = document.getElementById('carouselPrev');
  
  if (nextBtn) {
    nextBtn.onclick = function() {
      currentIndex++;
      updateCarousel();
    };
  }
  
  if (prevBtn) {
    prevBtn.onclick = function() {
      currentIndex--;
      updateCarousel();
    };
  }

  // Responsive: recalculate cardWidth and update on resize
  function handleResize() {
    cardWidth = cards[0]?.offsetWidth + 16 || 196;
    updateCarousel();
  }
  
  window.removeEventListener('resize', handleResize);
  window.addEventListener('resize', handleResize);

  // Initial position
  updateCarousel();
}

document.addEventListener('DOMContentLoaded', function() {
  initializeProductCarousel();
  loadCategoryCards();
});

// Function to load and display category cards
function loadCategoryCards() {
    const categoriesGrid = document.getElementById('categoriesGrid');
    if (!categoriesGrid) {
        console.error('Categories grid not found');
        return;
    }

    // Check if categories are already rendered server-side
    const serverRenderedCategories = categoriesGrid.querySelectorAll('.product-category-card[data-category]:not(.loading)');
    if (serverRenderedCategories.length > 0) {
        console.log('Categories already loaded from server, skipping AJAX fetch');
        // Hide loading placeholders
        const loadingCards = categoriesGrid.querySelectorAll('.product-category-card.loading');
        loadingCards.forEach(card => card.style.display = 'none');
        return;
    }

    // If no server-rendered categories, fetch via AJAX
    fetch('/categories/fetch')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch categories');
            }
            return response.json();
        })
        .then(categories => {
            displayCategoryCards(categories);
        })
        .catch(error => {
            console.error('Error fetching categories:', error);
            displayCategoryError();
        });
}

// Function to display category cards
function displayCategoryCards(categories) {
    const categoriesGrid = document.getElementById('categoriesGrid');
    if (!categoriesGrid) {
        console.error('Categories grid not found');
        return;
    }

    // Clear loading spinner
    categoriesGrid.innerHTML = '';
    
    // The CSS Grid layout is already applied via category-grid class in HTML
    // No need to modify styles here as they're handled by CSS

    // Create category cards
    categories.forEach((category, index) => {
        const categoryCard = createCategoryCard(category, index);
        categoriesGrid.appendChild(categoryCard);
    });
}

// Function to create individual category card
function createCategoryCard(category, index) {
    const card = document.createElement('div');
    card.className = 'product-category-card';
    card.setAttribute('data-category', category.name);
    
    // Use image URL from database if available, otherwise use default Grofers image
    const imageUrl = category.imageUrl || category.image_url || 'https://cdn.grofers.com/cdn-cgi/image/f=auto,fit=scale-down,q=85,metadata=none,w=225/layout-engine/2022-12/paan-corner_web.png';

    card.innerHTML = `
        <div class="card-image">
            <img src="${imageUrl}" 
                 alt="${category.name}" 
                 width="120" 
                 height="120" 
                 loading="lazy"
                 onerror="this.src='https://cdn.grofers.com/cdn-cgi/image/f=auto,fit=scale-down,q=85,metadata=none,w=225/layout-engine/2022-12/paan-corner_web.png'">
        </div>
        <div class="card-content">
            <h6 class="category-name">${category.name}</h6>
            <p class="category-description">${category.description || 'Browse products'}</p>
        </div>
    `;

    // Add click event to redirect to category products page
    card.addEventListener('click', function() {
        // Add click animation using CSS classes
        this.style.transform = 'scale(0.95)';
        setTimeout(() => {
            this.style.transform = '';
        }, 150);
        
        // Show subcategories for this category if available
        if (category.subcategories && category.subcategories.length > 0) {
            showSubcategoriesForCategory(category.name, category.subcategories);
            
            // Scroll to subcategories section
            setTimeout(() => {
                const subcategoriesSection = document.getElementById('subcategoriesCarousel');
                if (subcategoriesSection) {
                    subcategoriesSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
                }
            }, 300);
        } else {
            // Redirect to category products page if no subcategories
            setTimeout(() => {
                window.location.href = `/categories/products/${encodeURIComponent(category.name)}`;
            }, 200);
        }
    });

    return card;
}

// Function to display error when categories fail to load
function displayCategoryError() {
    const categoriesGrid = document.getElementById('categoriesGrid');
    if (categoriesGrid) {
        categoriesGrid.innerHTML = `
            <div class="col-12 text-center">
                <div class="alert alert-warning" role="alert">
                    <i class="fas fa-exclamation-triangle"></i>
                    Unable to load categories. Please try refreshing the page.
                </div>
            </div>
        `;
    }
}

// Dummy payment handler for UI demonstration
document.getElementById('paymentForm').addEventListener('submit', function(e) {
  e.preventDefault();
  document.getElementById('paymentStatus').innerHTML =
    '<div class="alert alert-info">Redirecting to payment gateway... (Demo only)</div>';
  // Here you would call your backend to initiate payment
});

let slideIndex = 0;
const slideDuration = 5000; // 5 seconds
let slideInterval;

showSlides();

function showSlides() {
  let i;
  const slides = document.getElementsByClassName("carousel-slide");
  const dots = document.getElementsByClassName("dot");
  const progressBars = document.getElementsByClassName("progress-bar");

  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
    dots[i].classList.remove("active");
    // Reset the animation by removing and re-adding the element
    const newProgressBar = progressBars[i].cloneNode(true);
    progressBars[i].parentNode.replaceChild(newProgressBar, progressBars[i]);
  }

  slideIndex++;
  if (slideIndex > slides.length) {
    slideIndex = 1;
  }

  slides[slideIndex - 1].style.display = "block";
  dots[slideIndex - 1].classList.add("active");

  // Clear the previous interval and start a new one
  clearInterval(slideInterval);
  slideInterval = setInterval(showSlides, slideDuration);
}

// Add click event listeners to the dots
const dotContainers = document.getElementsByClassName("dot-container");
for (let j = 0; j < dotContainers.length; j++) {
  dotContainers[j].addEventListener("click", function() {
    slideIndex = j;
    showSlides();
  });
}

document.addEventListener('DOMContentLoaded', function () {
  const cartCountElement = document.getElementById('cartCount');
  const cartItemsList = document.getElementById('cartItemsList');
  const cartBtn = document.getElementById('cartBtn');
  let cartCount = 0;
  let cartItems = []; // Array to store cart items

  // Add event listener to all "View" buttons
  const viewButtons = document.querySelectorAll('.view-btn');
  viewButtons.forEach(button => {
    button.addEventListener('click', function (event) {
      event.preventDefault(); // Prevent default link behavior

      // Increment the cart count
      cartCount++;
      cartCountElement.textContent = cartCount;

      // Get the item details
      const itemId = this.getAttribute('data-item-id');
      const itemName = this.closest('.card').querySelector('.card-title').textContent;
      const itemPrice = this.closest('.card').querySelector('.font-weight-bold.text-primary').textContent;

      // Add the item to the cart
      cartItems.push({ id: itemId, name: itemName, price: itemPrice });

      console.log(`Item with ID ${itemId} added to cart.`);
    });
  });

  // Show cart items in the modal
  cartBtn.addEventListener('click', function () {
    // Clear the current list
    cartItemsList.innerHTML = '';

    // Add each item to the list
    cartItems.forEach(item => {
      const listItem = document.createElement('li');
      listItem.className = 'list-group-item d-flex justify-content-between align-items-center';
      listItem.textContent = `${item.name} - ${item.price}`;
      cartItemsList.appendChild(listItem);
    });

    // Show the modal
    $('#cartModal').modal('show');
  });
});

document.addEventListener('DOMContentLoaded', function () {
  const createUserBtn = document.getElementById('createUserBtn');

  createUserBtn.addEventListener('click', function () {
    // Get user input
    const email = document.getElementById('form1Example13').value;
    const password = document.getElementById('form1Example23').value;

    // Validate input
    if (!email || !password) {
      alert('Please enter both email and password.');
      return;
    }

    // Send API request to create a new user
    fetch('/api/users', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
    })
      .then(response => {
        if (response.ok) {
          alert('User created successfully!');
          // Optionally, clear the form fields
          document.getElementById('form1Example13').value = '';
          document.getElementById('form1Example23').value = '';
        } else {
          throw new Error('Failed to create user.');
        }
      })
      .catch(error => {
        alert(error.message);
      });
  });
});


// Global carousel control variables
let carouselController = null;

$(document).ready(function() {
    let currentSlide = 0;
    const slides = $('.carousel-slide');
    const dots = $('.dot-container');
    const totalSlides = slides.length;
    let autoSlideInterval;
    let isTransitioning = false;

    // Initialize carousel
    function initCarousel() {
        slides.hide();
        slides.eq(0).show();
        updateDots(0);
        startAutoSlide();
    }

    // Show specific slide with smooth transition
    function showSlide(index, direction = 'next') {
        if (isTransitioning || index === currentSlide) return;
        
        isTransitioning = true;
        const currentSlideEl = slides.eq(currentSlide);
        const nextSlideEl = slides.eq(index);
        
        // Set initial position for next slide
        if (direction === 'next') {
            nextSlideEl.css({ left: '100%', display: 'block' });
        } else {
            nextSlideEl.css({ left: '-100%', display: 'block' });
        }
        
        // Animate current slide out and next slide in
        if (direction === 'next') {
            currentSlideEl.animate({ left: '-100%' }, 1600, 'swing');
            nextSlideEl.animate({ left: '0%' }, 1600, 'swing', function() {
                currentSlideEl.hide().css('left', '0%');
                isTransitioning = false;
            });
        } else {
            currentSlideEl.animate({ left: '100%' }, 1600, 'swing');
            nextSlideEl.animate({ left: '0%' }, 1600, 'swing', function() {
                currentSlideEl.hide().css('left', '0%');
                isTransitioning = false;
            });
        }
        
        currentSlide = index;
        updateDots(index);
        updateProgressBar(index);
    }

    // Update dot indicators
    function updateDots(activeIndex) {
        dots.removeClass('active');
        dots.eq(activeIndex).addClass('active');
    }

    // Update progress bar animation
    function updateProgressBar(activeIndex) {
        $('.progress-bar').removeClass('active');
        dots.eq(activeIndex).find('.progress-bar').addClass('active');
    }

    // Next slide
    function nextSlide() {
        const next = (currentSlide + 1) % totalSlides;
        showSlide(next, 'next');
    }

    // Previous slide
    function prevSlide() {
        const prev = (currentSlide - 1 + totalSlides) % totalSlides;
        showSlide(prev, 'prev');
    }

    // Auto slide functionality
    function startAutoSlide() {
        autoSlideInterval = setInterval(nextSlide, 4000); // Change slide every 4 seconds
    }

    function stopAutoSlide() {
        clearInterval(autoSlideInterval);
    }

    // Dot click handlers
    dots.each(function(index) {
        $(this).on('click', function() {
            if (index !== currentSlide) {
                const direction = index > currentSlide ? 'next' : 'prev';
                showSlide(index, direction);
                stopAutoSlide();
                setTimeout(startAutoSlide, 2000); // Restart auto-slide after 2 seconds
            }
        });
    });

    // Pause auto-slide on hover
    $('.carousel-container').hover(
        function() { stopAutoSlide(); },
        function() { startAutoSlide(); }
    );

    // Initialize the carousel
    initCarousel();
    
    // Expose control functions globally
    carouselController = {
        next: function() {
            nextSlide();
            stopAutoSlide();
            setTimeout(startAutoSlide, 2000);
        },
        prev: function() {
            prevSlide();
            stopAutoSlide();
            setTimeout(startAutoSlide, 2000);
        }
    };
});

// Global functions for manual navigation
function nextSlideManual() {
    if (carouselController) {
        carouselController.next();
    }
}

function prevSlideManual() {
    if (carouselController) {
        carouselController.prev();
    }
}


$(document).ready(function() {
    // Check if user is already authenticated from server-side
    const serverWelcomeMessage = $('#serverWelcomeMessage');
    if (serverWelcomeMessage.length > 0) {
        // Auto-hide server welcome message after 5 seconds
        setTimeout(() => {
            serverWelcomeMessage.fadeOut('slow');
        }, 5000);
    }
    
    // Handle login form submission
    $('#loginForm').on('submit', function(e) {
        e.preventDefault();
        
        const email = $('#form1Example13').val();
        const password = $('#form1Example23').val();
        
        if (!email || !password) {
            showLoginStatus('Please fill in all fields', 'danger');
            return;
        }
        
        // Show loading state
        const submitBtn = $(this).find('button[type="submit"]');
        const originalText = submitBtn.text();
        submitBtn.text('Signing in...').prop('disabled', true);
        
        // Make AJAX request
        $.ajax({
            url: '/api/login',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email,
                password: password
            }),
            success: function(response) {
                if (response.success) {
                    showLoginStatus('Login successful! Redirecting...', 'success');
                    
                    // Redirect to home page to refresh authentication state
                    setTimeout(function() {
                        window.location.href = '/home';
                    }, 1000);
                } else {
                    showLoginStatus(response.message || 'Login failed', 'danger');
                }
            },
            error: function(xhr, status, error) {
                showLoginStatus('Connection error. Please try again.', 'danger');
                console.error('Login error:', error);
            },
            complete: function() {
                // Reset button state
                submitBtn.text(originalText).prop('disabled', false);
            }
        });
    });
    
    function showLoginStatus(message, type) {
        const loginModal = $('#loginModal');
        let statusDiv = loginModal.find('#loginStatus');
        
        // Create status div if it doesn't exist
        if (statusDiv.length === 0) {
            statusDiv = $('<div id="loginStatus" class="mt-3"></div>');
            loginModal.find('.modal-body form').after(statusDiv);
        }
        
        statusDiv.html(`<div class="alert alert-${type}" role="alert">${message}</div>`);
        
        // Auto-hide success messages
        if (type === 'success') {
            setTimeout(() => statusDiv.empty(), 3000);
        }
    }
    
    // Handle create new user button
    $('#createUserBtn').on('click', function() {
        $('#loginModal').modal('hide');
        window.location.href = '/register';
    });
});

// Global logout function
function logout() {
    if (confirm('Are you sure you want to logout?')) {
        $.ajax({
            url: '/api/logout',
            method: 'POST',
            success: function(response) {
                if (response.success) {
                    // Redirect to home page
                    window.location.href = '/';
                } else {
                    alert('Logout failed. Please try again.');
                }
            },
            error: function() {
                // Fallback - redirect anyway
                window.location.href = '/';
            }
        });
    }
}

$(document).ready(function() {
    // Check if user is already authenticated from server-side
    const serverWelcomeMessage = $('#serverWelcomeMessage');
    if (serverWelcomeMessage.length > 0) {
        // Auto-hide server welcome message after 5 seconds
        setTimeout(() => {
            serverWelcomeMessage.fadeOut('slow');
        }, 5000);
    }
    
    // Handle login form submission
    $('#loginForm').on('submit', function(e) {
        e.preventDefault();
        
        const email = $('#form1Example13').val();
        const password = $('#form1Example23').val();
        
        if (!email || !password) {
            showLoginStatus('Please fill in all fields', 'danger');
            return;
        }
        
        // Show loading state
        const submitBtn = $(this).find('button[type="submit"]');
        const originalText = submitBtn.text();
        submitBtn.text('Signing in...').prop('disabled', true);
        
        // Make AJAX request
        $.ajax({
            url: '/api/login',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email,
                password: password
            }),
            success: function(response) {
                if (response.success) {
                    showLoginStatus('Login successful! Redirecting...', 'success');
                    
                    // Redirect to home page to refresh authentication state
                    setTimeout(function() {
                        window.location.href = '/home';
                    }, 1000);
                } else {
                    showLoginStatus(response.message || 'Login failed', 'danger');
                }
            },
            error: function(xhr, status, error) {
                showLoginStatus('Connection error. Please try again.', 'danger');
                console.error('Login error:', error);
            },
            complete: function() {
                // Reset button state
                submitBtn.text(originalText).prop('disabled', false);
            }
        });
    });
    
    function showLoginStatus(message, type) {
        const loginModal = $('#loginModal');
        let statusDiv = loginModal.find('#loginStatus');
        
        // Create status div if it doesn't exist
        if (statusDiv.length === 0) {
            statusDiv = $('<div id="loginStatus" class="mt-3"></div>');
            loginModal.find('.modal-body form').after(statusDiv);
        }
        
        statusDiv.html(`<div class="alert alert-${type}" role="alert">${message}</div>`);
        
        // Auto-hide success messages
        if (type === 'success') {
            setTimeout(() => statusDiv.empty(), 3000);
        }
    }
    
    // Handle create new user button
    $('#createUserBtn').on('click', function() {
        $('#loginModal').modal('hide');
        window.location.href = '/register';
    });
});

// Global logout function
function logout() {
    if (confirm('Are you sure you want to logout?')) {
        $.ajax({
            url: '/api/logout',
            method: 'POST',
            success: function(response) {
                if (response.success) {
                    // Redirect to home page
                    window.location.href = '/';
                } else {
                    alert('Logout failed. Please try again.');
                }
            },
            error: function() {
                // Fallback - redirect anyway
                window.location.href = '/';
            }
        });
    }
}

$(document).ready(function() {
    let currentSlide = 0;
    const slides = $('.carousel-slide');
    const dots = $('.dot-container');
    const totalSlides = slides.length;
    let autoSlideInterval;
    let isTransitioning = false;

    // Initialize carousel
    function initCarousel() {
        slides.hide();
        slides.eq(0).show();
        updateDots(0);
        startAutoSlide();
    }

    // Show specific slide with smooth transition
    function showSlide(index, direction = 'next') {
        if (isTransitioning || index === currentSlide) return;
        
        isTransitioning = true;
        const currentSlideEl = slides.eq(currentSlide);
        const nextSlideEl = slides.eq(index);
        
        // Set initial position for next slide
        if (direction === 'next') {
            nextSlideEl.css({ left: '100%', display: 'block' });
        } else {
            nextSlideEl.css({ left: '-100%', display: 'block' });
        }
        
        // Animate current slide out and next slide in
        if (direction === 'next') {
            currentSlideEl.animate({ left: '-100%' }, 600, 'swing');
            nextSlideEl.animate({ left: '0%' }, 600, 'swing', function() {
                currentSlideEl.hide().css('left', '0%');
                isTransitioning = false;
            });
        } else {
            currentSlideEl.animate({ left: '100%' }, 600, 'swing');
            nextSlideEl.animate({ left: '0%' }, 600, 'swing', function() {
                currentSlideEl.hide().css('left', '0%');
                isTransitioning = false;
            });
        }
        
        currentSlide = index;
        updateDots(index);
        updateProgressBar(index);
    }

    // Update dot indicators
    function updateDots(activeIndex) {
        dots.removeClass('active');
        dots.eq(activeIndex).addClass('active');
    }

    // Update progress bar animation
    function updateProgressBar(activeIndex) {
        $('.progress-bar').removeClass('active');
        dots.eq(activeIndex).find('.progress-bar').addClass('active');
    }

    // Next slide
    function nextSlide() {
        const next = (currentSlide + 1) % totalSlides;
        showSlide(next, 'next');
    }

    // Previous slide
    function prevSlide() {
        const prev = (currentSlide - 1 + totalSlides) % totalSlides;
        showSlide(prev, 'prev');
    }

    // Auto slide functionality
    function startAutoSlide() {
        autoSlideInterval = setInterval(nextSlide, 4000); // Change slide every 4 seconds
    }

    function stopAutoSlide() {
        clearInterval(autoSlideInterval);
    }

    // Dot click handlers
    dots.each(function(index) {
        $(this).on('click', function() {
            if (index !== currentSlide) {
                const direction = index > currentSlide ? 'next' : 'prev';
                showSlide(index, direction);
                stopAutoSlide();
                setTimeout(startAutoSlide, 2000); // Restart auto-slide after 2 seconds
            }
        });
    });

    // Pause auto-slide on hover
    $('.carousel-container').hover(
        function() { stopAutoSlide(); },
        function() { startAutoSlide(); }
    );

    // Initialize the carousel
    initCarousel();
    
    // Expose control functions globally
    carouselController = {
        next: function() {
            nextSlide();
            stopAutoSlide();
            setTimeout(startAutoSlide, 2000);
        },
        prev: function() {
            prevSlide();
            stopAutoSlide();
            setTimeout(startAutoSlide, 2000);
        }
    };
});

// Global functions for manual navigation
function nextSlideManual() {
    if (carouselController) {
        carouselController.next();
    }
}

function prevSlideManual() {
    if (carouselController) {
        carouselController.prev();
    }
}
