package com.propertyLah.repository;

import com.propertyLah.model.Property;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
	List<Property> findByTypeContainingOrLocationContainingOrDescriptionContaining(String type, String location, String description);
}


