package com.propertyLah.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @SequenceGenerator(name = "PROP_SEQ_GNTR", sequenceName = "PROP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROP_SEQ_GNTR")
    private Long id;

    private String type;
    private double price;
    private String location;
    private double latitude;
    private double longitude;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User owner;
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images;
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Tenant> tenants;

    public Property() {
        super();
    }

    public Property(Long id, String type, double price, String location, double latitude, double longitude, String description) {
        super();
        this.id = id;
        this.type = type;
        this.price = price;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public List<Image> getImages() {
        return images;
    }
    
    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }
}


