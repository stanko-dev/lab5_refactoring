package com.restaurant.controller;

import com.restaurant.dto.CustomerDTO;
import com.restaurant.dto.OrderDTO;
import com.restaurant.model.Dish;
import com.restaurant.service.RestaurantService;

import java.util.List;

public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public OrderDTO placeOrder(int customerId, List<String> dishNames) {
        return restaurantService.placeOrder(customerId, dishNames);
    }

    public OrderDTO cancelOrder(int orderId) {
        return restaurantService.cancelOrder(orderId);
    }

    public List<Dish> searchDishes(String name) {
        return restaurantService.findDishesByName(name);
    }

    public CustomerDTO registerCustomer(int id, String name) {
        return restaurantService.registerCustomer(id, name);
    }
}
