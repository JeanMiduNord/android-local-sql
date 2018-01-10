package com.example.formation.localsqlapp.model;



public class Contact {
    private String name;
    private String first_name;
    private String email;
    private Long id;

    public Contact() {
    }

    public Contact(String name, String first_name, String email) {
        this.name = name;
        this.first_name = first_name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public Contact setName(String name) {
        this.name = name;
        return this;
    }

    public String getFirst_name() {
        return first_name;
    }

    public Contact setFirst_name(String first_name) {
        this.first_name = first_name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Contact setEmail(String email) {
        this.email = email;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Contact setId(long id) {
        this.id = id;
        return this;
    }
}
