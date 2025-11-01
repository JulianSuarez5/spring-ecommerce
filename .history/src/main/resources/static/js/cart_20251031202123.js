const ECommerce = {
    addToCart: function(productId, quantity) {
        fetch(`/cart/add?productId=${productId}&quantity=${quantity}`, {
            method: 'POST'
        })
        .then(response => {
            if (!response.ok) throw new Error("Error al agregar al carrito");
            return response.json();
        })
        .then(data => {
            if (data.error) {
                alert(data.error);
                return;
            }
            // ✅ Actualizar el contador del carrito en el navbar
            const cartBadge = document.querySelector('#cart-count');
            if (cartBadge) cartBadge.textContent = data.count;

            // ✅ Animación visual
            Swal.fire({
                title: "¡Agregado!",
                text: data.message,
                icon: "success",
                timer: 1500,
                showConfirmButton: false
            });
        })
        .catch(err => {
            console.error(err);
            Swal.fire({
                title: "Error",
                text: "No se pudo agregar el producto al carrito",
                icon: "error"
            });
        });
    }
};
