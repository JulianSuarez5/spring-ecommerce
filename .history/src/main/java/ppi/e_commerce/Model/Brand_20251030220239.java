package ppi.e_commerce.Model;package ppi.e_commerce.Model;



import jakarta.persistence.*;import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;import jakarta.validation.constraints.Size;

import java.util.Date;import java.util.List;



@Entity@Entity

@Table(name = "brands")@Table(name = "brands")

public class Brand {public class Brand {

    @Id

    @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @GeneratedValue(strategy = GenerationType.IDENTITY)    private Integer id;

    private Integer id;

    @NotBlank(message = "El nombre de la marca es obligatorio")

    @NotBlank(message = "El nombre es obligatorio")    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")

    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")    @Column(unique = true, nullable = false)

    @Column(nullable = false)    private String name;

    private String name;

    @Size(max = 200, message = "La descripción no puede exceder 200 caracteres")

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")    private String description;

    private String description;

    private String logoUrl;

    @Size(max = 255, message = "La URL del logo no puede exceder los 255 caracteres")    private String website;

    private String logoUrl;    private boolean active = true;



    @Size(max = 255, message = "La URL del sitio web no puede exceder los 255 caracteres")    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private String website;    private List<Product> products;



    private Boolean active = true;    public Brand() {}



    @Column(name = "created_at")    public Brand(String name, String description) {

    @Temporal(TemporalType.TIMESTAMP)        this.name = name;

    private Date createdAt;        this.description = description;

    }

    @Column(name = "updated_at")

    @Temporal(TemporalType.TIMESTAMP)    public Brand(String name, String description, String logoUrl, String website) {

    private Date updatedAt;        this.name = name;

        this.description = description;

    @PrePersist        this.logoUrl = logoUrl;

    protected void onCreate() {        this.website = website;

        createdAt = new Date();    }

        updatedAt = new Date();

    }    public Brand(String name) {

        this.name = name;

    @PreUpdate    }

    protected void onUpdate() {

        updatedAt = new Date();    // Getters and Setters

    }    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    // Getters y Setters

    public Integer getId() {    public String getName() { return name; }

        return id;    public void setName(String name) { this.name = name; }

    }

    public String getDescription() { return description; }

    public void setId(Integer id) {    public void setDescription(String description) { this.description = description; }

        this.id = id;

    }    public String getLogoUrl() { return logoUrl; }

    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getName() {

        return name;    public String getWebsite() { return website; }

    }    public void setWebsite(String website) { this.website = website; }



    public void setName(String name) {    public boolean isActive() { return active; }

        this.name = name;    public boolean getActive() { return active; }

    }    public void setActive(boolean active) { this.active = active; }



    public String getDescription() {    public List<Product> getProducts() { return products; }

        return description;    public void setProducts(List<Product> products) { this.products = products; }

    }

    @Override

    public void setDescription(String description) {    public String toString() {

        this.description = description;        return "Brand{" +

    }                "id=" + id +

                ", name='" + name + '\'' +

    public String getLogoUrl() {                ", description='" + description + '\'' +

        return logoUrl;                ", website='" + website + '\'' +

    }                ", active=" + active +

                '}';

    public void setLogoUrl(String logoUrl) {    }

        this.logoUrl = logoUrl;}

    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", website='" + website + '\'' +
                ", active=" + active +
                '}';
    }
}