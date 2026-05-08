package com.restaurant.dto;

public class CustomerDTO {
    private final int id;
    private final String name;

    public CustomerDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "CustomerDTO{id=" + id + ", name='" + name + "'}";
    }
}
