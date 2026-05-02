package com.restaurant.model;

public class Dish {
    private final String name;
    private final double price;
    private final String category;

    public Dish(String name, double price, String category) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Dish name is required");
        if (price < 0) throw new IllegalArgumentException("Price must be non-negative");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("Category is required");
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }

    @Override
    public String toString() { return name + " (" + price + " грн, " + category + ")"; }
}
