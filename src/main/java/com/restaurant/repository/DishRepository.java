package com.restaurant.repository;

import com.restaurant.model.Dish;
import java.util.List;

public interface DishRepository {
    void save(Dish dish);
    List<Dish> findAll();
    List<Dish> findByName(String name);
}
