package ppi.e_commerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ppi.e_commerce.Model.*;
import ppi.e_commerce.Repository.OrderRepository;
import ppi.e_commerce.Repository.OrderDetailRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    /**
     * Crea un nuevo pedido a partir de los items del carrito
     */
    public Order createOrder(User user, List<CartItem> cartItems) {
        // Calcular el precio total
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        // Crear el pedido
        Order order = new Order();
        order.setNumber(generateOrderNumber());
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setCreationDate(LocalDateTime.now());
        order.setStatus("pending");

        // Guardar el pedido primero
        order = orderRepository.save(order);

        // Crear los detalles del pedido
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setName(item.getProduct().getName());
            detail.setPrice(item.getProduct().getPrice());
            detail.setQuantity(item.getQuantity());
            detail.setTotalPrice(item.getProduct().getPrice() * item.getQuantity());
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            orderDetails.add(detail);
        }

        // Guardar los detalles
        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails);

        return order;
    }

    /**
     * Genera un número único de pedido
     */
    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Obtiene todos los pedidos ordenados por fecha
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreationDateDesc();
    }

    /**
     * Obtiene un pedido por ID
     */
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    /**
     * Obtiene los pedidos de un usuario específico
     */
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreationDateDesc(user);
    }

    /**
     * Obtiene los pedidos más recientes (para el dashboard)
     */
    public List<Order> getRecentOrders(int limit) {
        List<Order> allOrders = orderRepository.findAllByOrderByCreationDateDesc();
        return allOrders.size() > limit ? allOrders.subList(0, limit) : allOrders;
    }

    /**
     * Actualiza el estado de un pedido
     */
    public Order updateOrderStatus(Integer orderId, String newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(newStatus);
            
            // Si el estado es "shipped", establecer fecha de envío
            if ("shipped".equals(newStatus) && order.getShippedDate() == null) {
                order.setShippedDate(LocalDateTime.now());
            }
            
            // Si el estado es "completed", establecer fecha de recepción
            if ("completed".equals(newStatus) && order.getReceiveDate() == null) {
                order.setReceiveDate(LocalDateTime.now());
            }
            
            return orderRepository.save(order);
        }
        return null;
    }

    /**
     * Actualiza un pedido completo
     */
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    /**
     * Elimina un pedido por ID
     */
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }

    /**
     * Cuenta el total de pedidos
     */
    public long countOrders() {
        return orderRepository.count();
    }

    /**
     * Obtiene el total de ventas (solo pedidos completados)
     */
    public Double getTotalSales() {
        return orderRepository.getTotalSales();
    }

    /**
     * Obtiene pedidos por estado
     */
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatusOrderByCreationDateDesc(status);
    }

    /**
     * Cuenta pedidos por estado
     */
    public Long countOrdersByStatus(String status) {
        return orderRepository.countByStatus(status);
    }

    /**
     * Obtiene pedidos de un usuario con un estado específico
     */
    public List<Order> getOrdersByUserAndStatus(User user, String status) {
        return orderRepository.findByUserAndStatus(user, status);
    }

    /**
     * Cuenta los pedidos de un usuario
     */
    public Long countOrdersByUser(User user) {
        return orderRepository.countByUser(user);
    }

    /**
     * Verifica si un pedido puede ser modificado
     */
    public boolean canModifyOrder(Order order) {
        return order != null && 
               !"completed".equals(order.getStatus()) && 
               !"cancelled".equals(order.getStatus());
    }

    /**
     * Obtiene estadísticas de pedidos para el dashboard
     */
    public OrderStatistics getOrderStatistics() {
        OrderStatistics stats = new OrderStatistics();
        stats.setTotalOrders(orderRepository.count());
        stats.setPendingOrders(countOrdersByStatus("pending"));
        stats.setShippedOrders(countOrdersByStatus("shipped"));
        stats.setCompletedOrders(countOrdersByStatus("completed"));
        stats.setCancelledOrders(countOrdersByStatus("cancelled"));
        stats.setTotalSales(getTotalSales());
        return stats;
    }

    // Clase interna para estadísticas (opcional)
    public static class OrderStatistics {
        private Long totalOrders;
        private Long pendingOrders;
        private Long shippedOrders;
        private Long completedOrders;
        private Long cancelledOrders;
        private Double totalSales;

        // Getters y Setters
        public Long getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }

        public Long getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(Long pendingOrders) { this.pendingOrders = pendingOrders; }

        public Long getShippedOrders() { return shippedOrders; }
        public void setShippedOrders(Long shippedOrders) { this.shippedOrders = shippedOrders; }

        public Long getCompletedOrders() { return completedOrders; }
        public void setCompletedOrders(Long completedOrders) { this.completedOrders = completedOrders; }

        public Long getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(Long cancelledOrders) { this.cancelledOrders = cancelledOrders; }

        public Double getTotalSales() { return totalSales; }
        public void setTotalSales(Double totalSales) { this.totalSales = totalSales; }
    }
}