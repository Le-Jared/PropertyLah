package com.propertyLah.ControllerTest;

import com.propertyLah.controller.TenantController;
import com.propertyLah.model.Property;
import com.propertyLah.model.Tenant;
import com.propertyLah.service.PropertyService;
import com.propertyLah.service.TenantService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TenantController.class)
public class TenantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenantService tenantService;

    @MockBean
    private PropertyService propertyService;

    @Test
    @WithMockUser
    public void testListTenants() throws Exception {
        Property property = new Property();
        property.setId(1L);
        Mockito.when(propertyService.getProperty(Mockito.anyLong())).thenReturn(property);

        mockMvc.perform(get("/properties/{propertyId}/tenants", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("tenants"))
                .andExpect(model().attributeExists("tenants"));
    }

    @Test
    @WithMockUser
    public void testListAllTenants() throws Exception {
        mockMvc.perform(get("/tenants/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("allTenants"))
                .andExpect(model().attributeExists("tenants"));
    }

    @Test
    @WithMockUser
    public void testShowNewTenantForm() throws Exception {
        Property property = new Property();
        property.setId(1L);
        Mockito.when(propertyService.getProperty(Mockito.anyLong())).thenReturn(property);

        mockMvc.perform(get("/properties/{propertyId}/tenants/new", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("new_tenant"))
                .andExpect(model().attributeExists("tenant"));
    }

    @Test
    @WithMockUser
    public void testCreateTenant() throws Exception {
        Tenant tenant = new Tenant();
        tenant.setId(1L);

        mockMvc.perform(post("/properties/{propertyId}/tenants/create", 1L)
                .flashAttr("tenant", tenant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/properties/*/tenants"));
    }

    @Test
    @WithMockUser
    public void testShowEditTenantForm() throws Exception {
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        Mockito.when(tenantService.getTenantById(Mockito.anyLong())).thenReturn(tenant);

        mockMvc.perform(get("/properties/{propertyId}/tenants/{tenantId}/edit", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_tenant"))
                .andExpect(model().attribute("tenant", tenant));
    }

    @Test
    @WithMockUser
    public void testUpdateTenant() throws Exception {
        Tenant tenant = new Tenant();
        tenant.setId(1L);

        mockMvc.perform(post("/properties/{propertyId}/tenants/{tenantId}/update", 1L, 1L)
                .flashAttr("tenant", tenant))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/properties/*/tenants"));
    }

    @Test
    @WithMockUser
    public void testDeleteTenant() throws Exception {
        mockMvc.perform(post("/properties/{propertyId}/tenants/{tenantId}/delete", 1L, 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/properties/*/tenants"));
    }
    
    @Test
    @WithMockUser
    public void testSearch() throws Exception {
        Tenant tenant1 = new Tenant();
        tenant1.setId(1L);
        Tenant tenant2 = new Tenant();
        tenant2.setId(2L);

        Mockito.when(tenantService.search("test")).thenReturn(Arrays.asList(tenant1, tenant2));

        mockMvc.perform(get("/tenants/search").param("search", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("allTenants"))
                .andExpect(model().attributeExists("tenants"));
    }
}

