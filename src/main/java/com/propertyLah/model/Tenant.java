package com.propertyLah.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @SequenceGenerator(name = "TENANT_SEQ_GNTR", sequenceName = "TENANT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TENANT_SEQ_GNTR")
    private Long id;

    private String name;
    private String phoneNumber;
    private double rent;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    @JsonIgnore
    private Property property;

    public Tenant() {
        super();
    }

    public Tenant(Long id, String name, String phoneNumber, double rent) {
        super();
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.rent = rent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}

