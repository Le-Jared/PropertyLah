package com.propertyLah.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.propertyLah.model.Image;
import com.propertyLah.model.Property;
import com.propertyLah.repository.ImageRepository;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    public Image getImage(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public List<Image> getImagesByProperty(Property property) {
        return imageRepository.findByProperty(property);
    }
}


