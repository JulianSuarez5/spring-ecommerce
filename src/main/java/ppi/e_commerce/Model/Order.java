package ppi.e_commerce.Model;

import java.util.Date;

public class Order {
    private Integer id;
    private String number;
    private Date creationDate;
    private Date receiveDate;
    private double totalPrice;

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