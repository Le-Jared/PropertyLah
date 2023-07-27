package com.propertyLah.service;

import com.propertyLah.model.Tenant;
import com.propertyLah.model.Property;
import com.propertyLah.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;
    private final PropertyService propertyService;


    @Autowired
    public TenantService(TenantRepository tenantRepository, PropertyService propertyService) {
        this.tenantRepository = tenantRepository;
        this.propertyService = propertyService;
    }

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public Tenant getTenantById(Long id) {
        return tenantRepository.findById(id).orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));
    }

    public Tenant saveTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    public void deleteTenant(Long id) {
        Tenant tenant = tenantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));      
        Property property = tenant.getProperty();
        property.getTenants().remove(tenant);
        propertyService.saveProperty(property);
        tenantRepository.delete(tenant);
    }
    
    public List<Tenant> search(String searchTerm) {
        return tenantRepository.search(searchTerm);
    }
}

