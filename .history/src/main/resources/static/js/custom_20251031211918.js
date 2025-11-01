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
                // SweetAlert2 con diseño moderno
                Swal.fire({
                    icon: 'success',
                    title: '¡Producto agregado!',
                    html: `
                        <div style="text-align: center;">
                            <p style="font-size: 1.1rem; margin-bottom: 8px;">
                                <strong>${data.productName}</strong>
                            </p>
                            <p style="color: #64748b; font-size: 0.95rem;">
                                Cantidad: <strong>${data.itemQuantity}</strong>
                            </p>
                        </div>
                    `,
                    showConfirmButton: true,
                    confirmButtonText: 'Ver carrito',
                    showCancelButton: true,
                    cancelButtonText: 'Seguir comprando',
                    confirmButtonColor: '#667eea',
                    cancelButtonColor: '#6c757d',
                    timer: 3000,
                    timerProgressBar: true,
                    toast: false,
                    position: 'center',
                    backdrop: true,
                    customClass: {
                        popup: 'animated-popup'
                    }
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = '/cart';
                    }
                });

                // Actualizar contador del carrito
                const cartCount = document.getElementById('cartItemCount');
                if (cartCount) {
                    cartCount.textContent = data.cartItemCount;
                    
                    // Animación del contador
                    cartCount.classList.add('cart-bounce');
                    setTimeout(() => {
                        cartCount.classList.remove('cart-bounce');
                    }, 600);
                }

            } else if (data.status === 'error' && data.redirectUrl) {
                // Redirect a login si no está autenticado
                Swal.fire({
                    icon: 'warning',
                    title: 'Inicia sesión',
                    text: data.message,
                    confirmButtonColor: '#667eea',
                    confirmButtonText: 'Ir a login'
                }).then(() => {
                    window.location.href = data.redirectUrl;
                });

            } else {
                // Error general
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: data.message || 'No se pudo agregar el producto',
                    confirmButtonColor: '#dc2626',
                    timer: 3000
                });
            }

        } catch (error) {
            console.error('Error al agregar producto:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No se pudo conectar con el servidor',
                confirmButtonColor: '#dc2626'
            });
        }
    },

    // Método alternativo para notificaciones simples
    showNotification: function(message, type = 'success') {
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer)
                toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
        });

        const iconMap = {
            'success': 'success',
            'danger': 'error',
            'warning': 'warning',
            'info': 'info'
        };

        Toast.fire({
            icon: iconMap[type] || 'info',
            title: message
        });
    }
};

// CSS para animación del contador del carrito
const style = document.createElement('style');
style.textContent = `
    @keyframes cartBounce {
        0%, 100% {
            transform: scale(1);
        }
        25% {
            transform: scale(1.3) rotate(-10deg);
        }
        50% {
            transform: scale(1.1) rotate(5deg);
        }
        75% {
            transform: scale(1.2) rotate(-5deg);
        }
    }

    .cart-bounce {
        animation: cartBounce 0.6s ease-in-out;
    }

    /* Estilo personalizado para SweetAlert */
    .animated-popup {
        animation: slideInDown 0.3s ease-out;
    }

    @keyframes slideInDown {
        from {
            opacity: 0;
            transform: translateY(-30px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    /* Mejorar apariencia de los botones de SweetAlert */
    .swal2-confirm, .swal2-cancel {
        border-radius: 8px !important;
        font-weight: 600 !important;
        padding: 10px 24px !important;
        transition: all 0.3s ease !important;
    }

    .swal2-confirm:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4) !important;
    }

    .swal2-timer-progress-bar {
        background: #667eea !important;
    }
`;
document.head.appendChild(style);