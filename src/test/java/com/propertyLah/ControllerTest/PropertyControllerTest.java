package com.propertyLah.ControllerTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.propertyLah.model.Property;
import com.propertyLah.service.PropertyService;
import com.propertyLah.service.UserService;
import com.propertyLah.service.ImageService;
import com.propertyLah.controller.PropertyController;
import com.propertyLah.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyController.class)
public class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private UserService userService;

    @MockBean
    private ImageService imageService;

    @Test
    @WithMockUser
    public void testShowNewPropertyForm() throws Exception {
        mockMvc.perform(get("/property/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("new_property"))
                .andExpect(model().attributeExists("property"));
    }

    @Test
    @WithMockUser
    public void testCreateProperty() throws Exception {
        Property property = new Property();
        property.setId(1L);

        mockMvc.perform(post("/property/new")
                .flashAttr("property", property))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));
    }

    @Test
    @WithMockUser
    public void testShowEditPropertyForm() throws Exception {
        Property property = new Property();
        property.setId(1L);
        Mockito.when(propertyService.getProperty(Mockito.anyLong())).thenReturn(property);

        mockMvc.perform(get("/property/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_property"))
                .andExpect(model().attribute("property", property));
    }

    @Test
    @WithMockUser
    public void testUpdateProperty() throws Exception {
        Property property = new Property();
        property.setId(1L);

        mockMvc.perform(post("/property/edit")
                .flashAttr("property", property))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));
    }

    @Test
    @WithMockUser
    public void testDeleteProperty() throws Exception {
        mockMvc.perform(get("/property/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));
    }

    @Test
    @WithMockUser
    public void testViewAllProperties() throws Exception {
        Property property1 = new Property();
        property1.setId(1L);
        Property property2 = new Property();
        property2.setId(2L);

        Mockito.when(propertyService.getAllProperties()).thenReturn(Arrays.asList(property1, property2));

        mockMvc.perform(get("/properties"))
                .andExpect(status().isOk())
                .andExpect(view().name("properties"))
                .andExpect(model().attributeExists("properties"));
    }

    @Test
    @WithMockUser
    public void testCreateProperty_withFileUpload() throws Exception {
        Property property = new Property();
        property.setId(1L);
        User user = new User();
        user.setEmail("test@test.com");
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getName()).thenReturn("test@test.com");
        when(userService.findUserByEmail(anyString())).thenReturn(user);
        when(propertyService.saveProperty(any(Property.class))).thenReturn(property);

        mockMvc.perform(multipart("/property/new")
                .file("images", "test.jpg".getBytes())
                .flashAttr("property", property))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));
    }

    @Test
    @WithMockUser
    public void testUpdateProperty_withFileUpload() throws Exception {
        Property property = new Property();
        property.setId(1L);
        User user = new User();
        user.setEmail("test@test.com");
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getName()).thenReturn("test@test.com");
        when(userService.findUserByEmail(anyString())).thenReturn(user);
        when(propertyService.getProperty(anyLong())).thenReturn(property);

        mockMvc.perform(multipart("/property/edit")
                .file("images", "test.jpg".getBytes())
                .flashAttr("property", property))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/properties"));
    }

    @Test
    @WithMockUser
    public void testViewAllProperties_withSearch() throws Exception {
        Property property1 = new Property();
        property1.setId(1L);
        Property property2 = new Property();
        property2.setId(2L);

        when(propertyService.searchProperties("test")).thenReturn(Arrays.asList(property1, property2));

        mockMvc.perform(get("/properties/search").param("search", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("properties"))
                .andExpect(model().attributeExists("properties"));
    }

    @Test
    @WithMockUser
    public void testShowMap() throws Exception {
        mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andExpect(view().name("map"));
    }

    @Test
    @WithMockUser
    public void testShowMortgage() throws Exception {
        mockMvc.perform(get("/mortgage"))
                .andExpect(status().isOk())
                .andExpect(view().name("mortgage"));
    }
}

