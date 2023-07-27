package com.propertyLah.ServiceTest;

import com.propertyLah.model.Image;
import com.propertyLah.model.Property;
import com.propertyLah.repository.ImageRepository;
import com.propertyLah.service.ImageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @MockBean
    private ImageRepository imageRepository;

    @Test
    public void testSaveImage() {
        Image image = new Image();
        image.setId(1L);

        Mockito.when(imageRepository.save(Mockito.any(Image.class))).thenReturn(image);

        Image created = imageService.saveImage(image);
        assertNotNull(created);
        assertEquals(image.getId(), created.getId());
    }

    @Test
    public void testDeleteImage() {
        imageService.deleteImage(1L);
        Mockito.verify(imageRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void testGetImage() {
        Image image = new Image();
        image.setId(1L);

        Mockito.when(imageRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(image));

        Image found = imageService.getImage(1L);
        assertNotNull(found);
        assertEquals(image.getId(), found.getId());
    }

    @Test
    public void testGetImagesByProperty() {
        Property property = new Property();
        property.setId(1L);
        Image image1 = new Image();
        image1.setId(1L);
        Image image2 = new Image();
        image2.setId(2L);

        Mockito.when(imageRepository.findByProperty(Mockito.any(Property.class))).thenReturn(Arrays.asList(image1, image2));

        List<Image> images = imageService.getImagesByProperty(property);
        assertNotNull(images);
        assertEquals(2, images.size());
    }
}

