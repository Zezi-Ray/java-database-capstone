package com.project.back_end.repo;

import com.project.back_end.models.Admin;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

// Extend JpaRepository:
//  - The repository extends JpaRepository<Admin, Long>, which gives it basic CRUD functionality.
//  - The methods such as save, delete, update, and find are inherited without the need for explicit implementation.
//  - JpaRepository also includes pagination and sorting features.
@Repository
// Add @Repository annotation:
//  - The @Repository annotation marks this interface as a Spring Data JPA repository.
//  - While it is technically optional (since JpaRepository is a part of Spring Data), it's good practice to include it for clarity.
//  - Spring Data JPA automatically implements the repository, providing the necessary CRUD functionality.
public interface AdminRepository extends JpaRepository<Admin, Long> {
    public Admin findByUsername(String username);
}


