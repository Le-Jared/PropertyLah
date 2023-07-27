package com.propertyLah.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.propertyLah.model.Property;
import com.propertyLah.service.PropertyService;

@RestController
public class PropertyRestController {

    private final PropertyService propertyService;

    public PropertyRestController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/api/properties")
    public List<Property> getAllProperties() {
        return propertyService.getAllProperties();
    }
}
