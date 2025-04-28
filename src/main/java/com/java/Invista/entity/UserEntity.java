package com.java.Invista.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    private String id;
    private String username;

    public UserEntity(String username, String id) {
        this.username = username;
        this.id = id;
    }
    public UserEntity() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
