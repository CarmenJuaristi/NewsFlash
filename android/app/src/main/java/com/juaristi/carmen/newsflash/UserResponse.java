package com.juaristi.carmen.newsflash;

public class UserResponse {
    private int id;
    private String username;
    private String token;
    private String email;

    // Constructor con parámetros para inicializar los campos
    public UserResponse(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Métodos getter y setter
    public int getId() {
        return id;
    }
    public String getToken() {
        return token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
