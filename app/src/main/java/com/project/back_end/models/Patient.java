package com.project.back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
// @Entity annotation:
//    - Marks the class as a JPA entity, meaning it represents a table in the database.
//    - Required for persistence frameworks (e.g., Hibernate) to map the class to a database table.
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 1. 'id' field:
    //    - Type: private Long
    //    - Description:
    //      - Represents the unique identifier for each patient.
    //      - The @Id annotation marks it as the primary key.
    //      - The @GeneratedValue(strategy = GenerationType.IDENTITY) annotation auto-generates the ID value when a new record is inserted into the database.

    @NotNull
    @Size(min = 3, max = 100)
    private String name;
    // 2. 'name' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the patient's full name.
    //      - The @NotNull annotation ensures that the patient's name is required.
    //      - The @Size(min = 3, max = 100) annotation ensures that the name length is between 3 and 100 characters. 
    //      - Provides validation for correct input and user experience.

    @NotNull
    @Size(max = 100)
    private String email;
    // 3. 'email' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the patient's email address.
    //      - The @NotNull annotation ensures that an email address must be provided.
    //      - The @Email annotation validates that the email address follows a valid email format (e.g., patient@example.com).

    @NotNull
    @Size(min = 6)
    private String password;
    // 4. 'password' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the patient's password for login authentication.
    //      - The @NotNull annotation ensures that a password must be provided.
    //      - The @Size(min = 6) annotation ensures that the password must be at least 6 characters long.

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    private String phone;
    // 5. 'phone' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the patient's phone number.
    //      - The @NotNull annotation ensures that a phone number must be provided.
    //      - The @Pattern(regexp = "^[0-9]{11}$") annotation validates that the phone number must be exactly 11 digits long.

    @NotNull
    @Size(max = 255)
    private String address;
    // 6. 'address' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the patient's address.
    //      - The @NotNull annotation ensures that the address must be provided.
    //      - The @Size(max = 255) annotation ensures that the address does not exceed 255 characters in length, providing validation for the address input.

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }   

    public String getPhone() {
        return phone;
    }   
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    // 7. Getters and Setters:
    //    - Standard getter and setter methods are provided for all fields: id, name, email, password, phone, and address.
    //    - These methods allow access and modification of the fields of the Patient class.

  

}
