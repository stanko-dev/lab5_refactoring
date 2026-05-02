package com.restaurant;

import com.restaurant.dto.CustomerDTO;
import com.restaurant.dto.OrderDTO;
import com.restaurant.model.Dish;
import com.restaurant.repository.InMemoryCustomerRepository;
import com.restaurant.repository.InMemoryDishRepository;
import com.restaurant.repository.InMemoryOrderRepository;
import com.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceTest {

    private RestaurantService service;

    @BeforeEach
    void setUp() {
        InMemoryOrderRepository orderRepo = new InMemoryOrderRepository();
        InMemoryCustomerRepository customerRepo = new InMemoryCustomerRepository();
        InMemoryDishRepository dishRepo = new InMemoryDishRepository();

        dishRepo.save(new Dish("Піца Маргарита", 180.0, "Піца"));
        dishRepo.save(new Dish("Піца Пепероні", 210.0, "Піца"));
        dishRepo.save(new Dish("Борщ", 95.0, "Супи"));

        service = new RestaurantService(orderRepo, customerRepo, dishRepo);
        service.registerCustomer(1, "Іван Петренко");
    }

    // --- Сценарій 1: Розміщення замовлення ---

    @Test
    void placeOrder_success() {
        OrderDTO order = service.placeOrder(1, List.of("Піца Маргарита"));
        assertNotNull(order);
        assertEquals("Іван Петренко", order.customerName);
        assertEquals("PENDING", order.status);
        assertEquals(180.0, order.totalPrice);
    }

    @Test
    void placeOrder_customerNotFound_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.placeOrder(99, List.of("Піца Маргарита")));
    }

    @Test
    void placeOrder_emptyDishes_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.placeOrder(1, List.of()));
    }

    @Test
    void placeOrder_unknownDish_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.placeOrder(1, List.of("Невідома страва")));
    }

    // --- Сценарій 2: Скасування замовлення ---

    @Test
    void cancelOrder_success() {
        OrderDTO placed = service.placeOrder(1, List.of("Борщ"));
        OrderDTO cancelled = service.cancelOrder(placed.orderId);
        assertEquals("CANCELLED", cancelled.status);
    }

    @Test
    void cancelOrder_alreadyCancelled_throws() {
        OrderDTO placed = service.placeOrder(1, List.of("Борщ"));
        service.cancelOrder(placed.orderId);
        assertThrows(IllegalStateException.class,
                () -> service.cancelOrder(placed.orderId));
    }

    @Test
    void cancelOrder_notFound_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cancelOrder(999));
    }

    // --- Сценарій 3: Пошук страв ---

    @Test
    void findDishesByName_returnsMatches() {
        List<Dish> result = service.findDishesByName("Піца");
        assertEquals(2, result.size());
    }

    @Test
    void findDishesByName_noMatches_returnsEmpty() {
        List<Dish> result = service.findDishesByName("Суші");
        assertTrue(result.isEmpty());
    }

    @Test
    void findDishesByName_blankQuery_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findDishesByName("  "));
    }

    // --- Сценарій 4: Реєстрація клієнта ---

    @Test
    void registerCustomer_success() {
        CustomerDTO dto = service.registerCustomer(2, "Марія Коваленко");
        assertEquals(2, dto.id);
        assertEquals("Марія Коваленко", dto.name);
    }

    @Test
    void registerCustomer_duplicateId_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerCustomer(1, "Дублікат"));
    }

    @Test
    void registerCustomer_blankName_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerCustomer(3, "  "));
    }
}
