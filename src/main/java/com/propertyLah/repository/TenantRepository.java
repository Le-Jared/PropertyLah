package com.propertyLah.repository;

import com.propertyLah.model.Tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
	@Query("SELECT t FROM Tenant t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR t.phoneNumber LIKE CONCAT('%', :searchTerm, '%')")
    List<Tenant> search(String searchTerm);
}

