package com.propertyLah.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.propertyLah.model.Image;
import com.propertyLah.model.Property;

import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    List<Image> findByProperty(Property property);
}


