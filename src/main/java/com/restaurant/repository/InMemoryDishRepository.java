package com.restaurant.repository;

import com.restaurant.model.Dish;
import java.util.ArrayList;
import java.util.List;

public class InMemoryDishRepository implements DishRepository {
    private final List<Dish> dishes = new ArrayList<>();

    @Override
    public void save(Dish dish) {
        if (dish == null) throw new IllegalArgumentException("Dish cannot be null");
        dishes.add(dish);
    }

    @Override
    public List<Dish> findAll() {
        return List.copyOf(dishes);
    }

    @Override
    public List<Dish> findByName(String name) {
        if (name == null || name.isBlank()) return List.of();
        String lower = name.toLowerCase();
        return dishes.stream()
                .filter(d -> d.getName().toLowerCase().contains(lower))
                .toList();
    }
}
