const ECommerce = {
    addToCart: async function(productId, quantity = 1) {
        const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

        try {
            const response = await fetch('/cart/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    [csrfHeader]: csrfToken
                },
                body: new URLSearchParams({
                    productId: productId,
                    quantity: quantity
                })
            });

            const data = await response.json();

            if (data.status === 'success') {
                ECommerce.showNotification(`✅ ${data.productName} agregado al carrito`, 'success');
                const cartCount = document.getElementById('cartItemCount');
                if (cartCount) cartCount.textContent = data.cartItemCount;
            } else if (data.status === 'error' && data.redirectUrl) {
                window.location.href = data.redirectUrl;
            } else {
                ECommerce.showNotification(`⚠️ ${data.message}`, 'warning');
            }
        } catch (error) {
            console.error(error);
            ECommerce.showNotification('❌ Error al agregar el producto', 'danger');
        }
    },

    showNotification: function(message, type) {
        const alert = document.createElement('div');
        alert.className = `alert alert-${type} position-fixed top-0 end-0 m-3 shadow fade show`;
        alert.textContent = message;
        alert.style.zIndex = '2000';
        document.body.appendChild(alert);
        setTimeout(() => alert.remove(), 3000);
    }
};
