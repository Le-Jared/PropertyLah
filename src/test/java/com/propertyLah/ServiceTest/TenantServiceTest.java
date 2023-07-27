package com.propertyLah.ServiceTest;

import com.propertyLah.model.Tenant;
import com.propertyLah.model.Property;
import com.propertyLah.repository.TenantRepository;
import com.propertyLah.service.PropertyService;
import com.propertyLah.service.TenantService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TenantServiceTest {

    @Autowired
    private TenantService tenantService;

    @MockBean
    private TenantRepository tenantRepository;

    @MockBean
    private PropertyService propertyService;

    @Test
    public void testGetAllTenants() {
        Tenant tenant1 = new Tenant();
        tenant1.setId(1L);
        Tenant tenant2 = new Tenant();
        tenant2.setId(2L);

        Mockito.when(tenantRepository.findAll()).thenReturn(Arrays.asList(tenant1, tenant2));

        List<Tenant> tenants = tenantService.getAllTenants();
        assertNotNull(tenants);
        assertEquals(2, tenants.size());
    }

    @Test
    public void testGetTenantById() {
        Tenant tenant = new Tenant();
        tenant.setId(1L);

        Mockito.when(tenantRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(tenant));

        Tenant found = tenantService.getTenantById(1L);
        assertNotNull(found);
        assertEquals(tenant.getId(), found.getId());
    }

    @Test
    public void testGetTenantById_NotFound() {
        Mockito.when(tenantRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> tenantService.getTenantById(1L));
    }

    @Test
    public void testSaveTenant() {
        Tenant tenant = new Tenant();
        tenant.setId(1L);

        Mockito.when(tenantRepository.save(Mockito.any(Tenant.class))).thenReturn(tenant);

        Tenant saved = tenantService.saveTenant(tenant);
        assertNotNull(saved);
        assertEquals(tenant.getId(), saved.getId());
    }

    @Test
    public void testDeleteTenant() {
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        Property property = new Property();
        property.setId(1L);
        property.setTenants(new ArrayList<>(Arrays.asList(tenant)));
        tenant.setProperty(property);

        Mockito.when(tenantRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(tenant));
        Mockito.when(propertyService.saveProperty(Mockito.any(Property.class))).thenReturn(property);

        tenantService.deleteTenant(1L);
        Mockito.verify(tenantRepository, Mockito.times(1)).delete(tenant);
    }

    
    @Test
    public void testSearch() {
        Tenant tenant1 = new Tenant();
        tenant1.setId(1L);
        Tenant tenant2 = new Tenant();
        tenant2.setId(2L);

        Mockito.when(tenantRepository.search("test")).thenReturn(Arrays.asList(tenant1, tenant2));

        List<Tenant> tenants = tenantService.search("test");
        assertNotNull(tenants);
        assertEquals(2, tenants.size());
    }
}

