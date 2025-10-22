package ppi.e_commerce.Model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String number;
    private Date creationDate;
    private Date receiveDate;
    private double totalPrice;

    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "order")
    private OrderDetail orderDetail;

    public Order() {
    }

    public Order(Integer id, String number, Date creationDate, Date receiveDate, double totalPrice) {
        this.id = id;
        this.number = number;
        this.creationDate = creationDate;
        this.receiveDate = receiveDate;
        this.totalPrice = totalPrice;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", receiveDate=" + receiveDate +
                ", totalPrice=" + totalPrice +
                '}';
    }
}