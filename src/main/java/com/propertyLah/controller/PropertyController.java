package com.propertyLah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;
import com.propertyLah.model.Image;
import com.propertyLah.model.Property;
import com.propertyLah.model.User;
import com.propertyLah.service.ImageService;
import com.propertyLah.service.PropertyService;
import com.propertyLah.service.UserService;
import com.propertyLah.util.FileUploadUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class PropertyController {

    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ImageService imageService;

    @GetMapping("/property/new")
    public String showNewPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        return "new_property";
    }

    @PostMapping("/property/new")
    public String createProperty(@ModelAttribute("property") Property property, @RequestParam("images") MultipartFile[] images, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if(user == null){
            return "redirect:/user/login";
        }
        property.setOwner(user);  
        property = propertyService.saveProperty(property);

        for (MultipartFile file : images) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            try {
                FileUploadUtil.saveFile(fileName, file);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload file. Please try again.");
                return "redirect:/property/new";
            }

            Image image = new Image();
            image.setName(fileName);
            image.setUrl("/img/" + fileName);
            image.setProperty(property);
            imageService.saveImage(image);  
        }

        return "redirect:/properties"; 
    }

    @GetMapping("/property/edit/{id}")
    public String showEditPropertyForm(@PathVariable("id") Long id, Model model) {
        Property property = propertyService.getProperty(id);
        model.addAttribute("property", property);
        return "edit_property";
    }

    @PostMapping("/property/edit")
    public String updateProperty(@ModelAttribute("property") Property property, @RequestParam(value = "images", required = false) MultipartFile[] images, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if(user == null){
            return "redirect:/user/login";
        }
        Property existingProperty = propertyService.getProperty(property.getId());
        if(existingProperty == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Property not found. Please try again.");
            return "redirect:/property/edit/" + property.getId();
        }
        existingProperty.setType(property.getType());
        existingProperty.setPrice(property.getPrice());
        existingProperty.setLocation(property.getLocation());
        existingProperty.setLatitude(property.getLatitude());
        existingProperty.setLongitude(property.getLongitude());
        existingProperty.setDescription(property.getDescription());
        existingProperty.setOwner(user);
        propertyService.updateProperty(existingProperty);
        
        // If new images are uploaded, save them
        if (images != null && images.length > 0 && !images[0].isEmpty()) {
            for (MultipartFile file : images) {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());

                try {
                    FileUploadUtil.saveFile(fileName, file);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload file. Please try again.");
                    return "redirect:/property/edit/" + property.getId();
                }

                Image image = new Image();
                image.setName(fileName);
                image.setUrl("/img/" + fileName);
                image.setProperty(property);
                imageService.saveImage(image);  
            }
        }
        return "redirect:/properties"; 
    }

    @GetMapping("/property/delete/{id}")
    public String deleteProperty(@PathVariable("id") Long id) {
        propertyService.deleteProperty(id);
        return "redirect:/properties"; 
    }
    
    @GetMapping("/properties")
    public String viewAllProperties(Model model) {
        List<Property> propertyList = propertyService.getAllProperties(); 
        Map<Property, List<Image>> propertyImages = new HashMap<>();
        for (Property property : propertyList) {
            List<Image> images = imageService.getImagesByProperty(property);
            propertyImages.put(property, images);
        }
        System.out.println(propertyImages);
        model.addAttribute("properties", propertyImages);
        return "properties"; 
    }

    
    @GetMapping("/properties/search")
    public String viewAllProperties(@RequestParam(required = false) String search, Model model) {
        List<Property> propertyList;
        if (search != null) {
            propertyList = propertyService.searchProperties(search);
        } else {
            propertyList = propertyService.getAllProperties(); 
        }
        model.addAttribute("properties", propertyList);
        return "properties"; 
    }
    
    
    @GetMapping("/map")
    public String showMap() {
        return "map";
    }
    
    @GetMapping("/mortgage")
    public String showMortgage() {
        return "mortgage";
    }
}



