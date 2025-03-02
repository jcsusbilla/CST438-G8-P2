package com.example.rest_service.database.entities;

import jakarta.persistence.*;

/**
 * 📌 Schema (Entity) For `user` Table
 * - This maps the Java class to the **database table**.
 * - The table name must be explicitly set as `user` because "User" is a reserved word in some SQL databases.
 */
@Entity
@Table(name = "user") // 🔹 This makes sure the table name is `user`, not `users`
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO) // This auto-generates an ID when a new user is added
    private Integer id;

    @Column(unique=true)
    private String userName; // Column: user_name

    private String password; // Column: password (This stores the **hashed** password, not plain text)

    @Column(unique = true)
    private String email; // Column: email

    private String firstName;
    private String lastName;



    private String getFirstName(){
        return firstName;
    }

    private String getLastName(){
        return lastName;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
