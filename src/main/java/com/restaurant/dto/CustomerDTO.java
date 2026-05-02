package com.restaurant.dto;

public class CustomerDTO {
    public final int id;
    public final String name;

    public CustomerDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CustomerDTO{id=" + id + ", name='" + name + "'}";
    }
}
