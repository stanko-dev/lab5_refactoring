package com.restaurant;

import com.restaurant.controller.RestaurantController;
import com.restaurant.dto.CustomerDTO;
import com.restaurant.dto.OrderDTO;
import com.restaurant.model.Dish;
import com.restaurant.repository.InMemoryCustomerRepository;
import com.restaurant.repository.InMemoryDishRepository;
import com.restaurant.repository.InMemoryOrderRepository;
import com.restaurant.service.RestaurantService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        InMemoryOrderRepository orderRepo = new InMemoryOrderRepository();
        InMemoryCustomerRepository customerRepo = new InMemoryCustomerRepository();
        InMemoryDishRepository dishRepo = new InMemoryDishRepository();

        dishRepo.save(new Dish("Піца Маргарита", 180.0, "Піца"));
        dishRepo.save(new Dish("Піца Пепероні", 210.0, "Піца"));
        dishRepo.save(new Dish("Борщ", 95.0, "Супи"));

        RestaurantService service = new RestaurantService(orderRepo, customerRepo, dishRepo);
        RestaurantController controller = new RestaurantController(service);

        System.out.println("=== Сценарій 1: Реєстрація клієнта ===");
        CustomerDTO customer = controller.registerCustomer(1, "Іван Петренко");
        System.out.println("Зареєстровано: " + customer);

        System.out.println("\n=== Сценарій 2: Розміщення замовлення ===");
        OrderDTO order = controller.placeOrder(1, List.of("Піца Маргарита", "Борщ"));
        System.out.println("Замовлення: " + order);

        System.out.println("\n=== Сценарій 3: Пошук страв ===");
        List<Dish> found = controller.searchDishes("Піца");
        System.out.println("Знайдено: " + found.size() + " страви");
        found.forEach(d -> System.out.println("  " + d));

        System.out.println("\n=== Сценарій 4: Завершення замовлення ===");
        OrderDTO completed = controller.completeOrder(order.getOrderId());
        System.out.println("Статус: " + completed.getStatus());

        System.out.println("\n=== Сценарій 5: Скасування замовлення ===");
        OrderDTO order2 = controller.placeOrder(1, List.of("Борщ"));
        OrderDTO cancelled = controller.cancelOrder(order2.getOrderId());
        System.out.println("Статус: " + cancelled.getStatus());
    }
}
