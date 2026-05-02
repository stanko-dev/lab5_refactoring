package com.restaurant.model;

public class Customer {
    private final int id;
    private final String name;

    public Customer(int id, String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Customer name is required");
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() { return "Customer{id=" + id + ", name='" + name + "'}"; }
}
