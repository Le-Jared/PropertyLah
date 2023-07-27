package com.propertyLah.controller;

import com.propertyLah.model.Property;
import com.propertyLah.model.Tenant;
import com.propertyLah.service.PropertyService;
import com.propertyLah.service.TenantService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class TenantController {

    private final TenantService tenantService;
    private final PropertyService propertyService;

    @Autowired
    public TenantController(TenantService tenantService, PropertyService propertyService) {
        this.tenantService = tenantService;
        this.propertyService = propertyService;
    }

    @GetMapping("/properties/{propertyId}/tenants")
    public String listTenants(@PathVariable Long propertyId, Model model) {
        Property property = propertyService.getProperty(propertyId);
        if (property == null) {
            return "redirect:/error";
        }
        model.addAttribute("tenants", property.getTenants());
        model.addAttribute("propertyType", property.getType());
        model.addAttribute("propertyLocation", property.getLocation());
        return "tenants";
    }

    @GetMapping("/tenants/all")
    public String listAllTenants(Model model) {
        List<Tenant> tenants = tenantService.getAllTenants();
        model.addAttribute("tenants", tenants);
        return "allTenants";
    }

    @GetMapping("/properties/{propertyId}/tenants/new")
    public String showNewTenantForm(@PathVariable Long propertyId, Model model) {
        Property property = propertyService.getProperty(propertyId);
        if (property == null) {
            return "redirect:/error";
        }
        model.addAttribute("tenant", new Tenant());
        model.addAttribute("propertyId", property.getId()); 
        return "new_tenant";
    }

    @PostMapping("/properties/{propertyId}/tenants/create")
    public String createTenant(@PathVariable Long propertyId, @ModelAttribute("tenant") Tenant tenant) {
        Property property = propertyService.getProperty(propertyId);
        if (property == null) {
            return "redirect:/error";
        }
        tenant.setProperty(property);
        tenantService.saveTenant(tenant);
        return "redirect:/properties/{propertyId}/tenants";
    }

    @GetMapping("/properties/{propertyId}/tenants/{tenantId}/edit")
    public String showEditTenantForm(@PathVariable Long propertyId, @PathVariable Long tenantId, Model model) {
        Tenant tenant = tenantService.getTenantById(tenantId);
        if (tenant == null || !tenant.getProperty().getId().equals(propertyId)) {
            return "redirect:/error";
        }
        model.addAttribute("tenant", tenant);
        return "edit_tenant";
    }

    @PostMapping("/properties/{propertyId}/tenants/{tenantId}/update")
    public String updateTenant(@PathVariable Long propertyId, @PathVariable Long tenantId, @ModelAttribute("tenant") Tenant tenant) {
        Tenant existingTenant = tenantService.getTenantById(tenantId);
        if (existingTenant == null || !existingTenant.getProperty().getId().equals(propertyId)) {
            return "redirect:/error";
        }
        tenant.setId(existingTenant.getId());
        tenant.setProperty(existingTenant.getProperty());
        tenantService.saveTenant(tenant);
        return "redirect:/properties/" + propertyId + "/tenants";
    }

    @PostMapping("/properties/{propertyId}/tenants/{tenantId}/delete")
    public String deleteTenant(@PathVariable Long propertyId, @PathVariable Long tenantId) {
        Tenant tenant = tenantService.getTenantById(tenantId);
        if (tenant == null || !tenant.getProperty().getId().equals(propertyId)) {
            return "redirect:/error";
        }
        tenantService.deleteTenant(tenantId);
        return "redirect:/properties/" + propertyId + "/tenants";
    }
    
    @GetMapping("/tenants/search")
    public String search(@RequestParam("search") String searchTerm, Model model) {
        List<Tenant> tenants = tenantService.search(searchTerm);
        model.addAttribute("tenants", tenants);
        return "allTenants"; // return view name
    }
}



