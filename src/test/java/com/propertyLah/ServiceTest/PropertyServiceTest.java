package com.propertyLah.ServiceTest;

import com.propertyLah.model.Property;
import com.propertyLah.repository.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fdm.propertyLah.service.PropertyService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PropertyServiceTest {

    @Autowired
    private PropertyService propertyService;

    @MockBean
    private PropertyRepository propertyRepository;

    @Test
    public void testSaveProperty() {
        Property property = new Property();
        property.setId(1L);

        Mockito.when(propertyRepository.save(Mockito.any(Property.class))).thenReturn(property);

        Property created = propertyService.saveProperty(property);
        assertNotNull(created);
        assertEquals(property.getId(), created.getId());
    }

    @Test
    public void testUpdateProperty() {
        Property property = new Property();
        property.setId(1L);

        Mockito.when(propertyRepository.save(Mockito.any(Property.class))).thenReturn(property);

        Property updated = propertyService.updateProperty(property);
        assertNotNull(updated);
        assertEquals(property.getId(), updated.getId());
    }

    @Test
    public void testDeleteProperty() {
        propertyService.deleteProperty(1L);
        Mockito.verify(propertyRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void testGetProperty() {
        Property property = new Property();
        property.setId(1L);

        Mockito.when(propertyRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(property));

        Property found = propertyService.getProperty(1L);
        assertNotNull(found);
        assertEquals(property.getId(), found.getId());
    }

    @Test
    public void testGetAllProperties() {
        Property property1 = new Property();
        property1.setId(1L);
        Property property2 = new Property();
        property2.setId(2L);

        Mockito.when(propertyRepository.findAll()).thenReturn(Arrays.asList(property1, property2));

        List<Property> properties = propertyService.getAllProperties();
        assertNotNull(properties);
        assertEquals(2, properties.size());
    }

    @Test
    public void testSearchProperties() {
        Property property1 = new Property();
        property1.setId(1L);
        property1.setType("Apartment");
        Property property2 = new Property();
        property2.setId(2L);
        property2.setLocation("City");

        Mockito.when(propertyRepository.findByTypeContainingOrLocationContainingOrDescriptionContaining(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(Arrays.asList(property1, property2));

        List<Property> properties = propertyService.searchProperties("City");
        assertNotNull(properties);
        assertEquals(2, properties.size());
    }
    
    @Test
    public void testGetPropertyWithTenants() {
        Property property = new Property();
        property.setId(1L);

        Mockito.when(propertyRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(property));

        Property found = propertyService.getPropertyWithTenants(1L);
        assertNotNull(found);
        assertEquals(property.getId(), found.getId());
    }

    @Test
    public void testGetPropertyWithTenants_NotFound() {
        Mockito.when(propertyRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> propertyService.getPropertyWithTenants(1L));
    }
}

