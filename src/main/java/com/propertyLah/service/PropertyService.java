package com.propertyLah.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.propertyLah.model.Property;
import com.propertyLah.repository.PropertyRepository;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Property updateProperty(Property property) {
        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

    public Property getProperty(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }
    
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
    
    public List<Property> searchProperties(String searchTerm) {
        return propertyRepository.findByTypeContainingOrLocationContainingOrDescriptionContaining(searchTerm, searchTerm, searchTerm);
    }
    
    public Property getPropertyWithTenants(Long id) {
        return propertyRepository.findById(id).orElseThrow(() -> new RuntimeException("Property not found with id: " + id));
    }
}

