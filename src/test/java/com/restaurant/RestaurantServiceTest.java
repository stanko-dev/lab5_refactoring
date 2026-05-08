package com.restaurant;

import com.restaurant.dto.CustomerDTO;
import com.restaurant.dto.OrderDTO;
import com.restaurant.model.Customer;
import com.restaurant.model.Dish;
import com.restaurant.model.Order;
import com.restaurant.model.OrderStatus;
import com.restaurant.repository.CustomerRepository;
import com.restaurant.repository.DishRepository;
import com.restaurant.repository.OrderRepository;
import com.restaurant.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private OrderRepository orderRepo;
    @Mock
    private CustomerRepository customerRepo;
    @Mock
    private DishRepository dishRepo;

    @InjectMocks
    private RestaurantService service;

    private static Customer ivan() {
        return new Customer(1, "Іван");
    }

    private static Dish pizza() {
        return new Dish("Піца Маргарита", 180.0, "Піца");
    }

    private static Dish borscht() {
        return new Dish("Борщ", 95.0, "Супи");
    }

    private static Order pendingOrder() {
        return new Order(1, ivan(), List.of(pizza()));
    }

    // --- Сценарій 1: Розміщення замовлення ---

    @Test
    void placeOrder_success() {
        when(customerRepo.findById(1)).thenReturn(Optional.of(ivan()));
        when(dishRepo.findByName("Піца Маргарита")).thenReturn(List.of(pizza()));
        when(orderRepo.nextId()).thenReturn(1);

        OrderDTO result = service.placeOrder(1, List.of("Піца Маргарита"));

        assertEquals("Іван", result.getCustomerName());
        assertEquals("PENDING", result.getStatus());
        assertEquals(180.0, result.getTotalPrice());
        verify(orderRepo).save(any(Order.class));
    }

    @Test
    void placeOrder_customerNotFound_throws() {
        when(customerRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.placeOrder(99, List.of("Піца Маргарита")));
    }

    @Test
    void placeOrder_emptyDishes_throws() {
        when(customerRepo.findById(1)).thenReturn(Optional.of(ivan()));
        assertThrows(IllegalArgumentException.class,
                () -> service.placeOrder(1, List.of()));
    }

    @Test
    void placeOrder_unknownDish_throws() {
        when(customerRepo.findById(1)).thenReturn(Optional.of(ivan()));
        when(dishRepo.findByName("Невідома страва")).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class,
                () -> service.placeOrder(1, List.of("Невідома страва")));
    }

    @Test
    void placeOrder_multipleDishes_sumsTotal() {
        when(customerRepo.findById(1)).thenReturn(Optional.of(ivan()));
        when(dishRepo.findByName("Піца Маргарита")).thenReturn(List.of(pizza()));
        when(dishRepo.findByName("Борщ")).thenReturn(List.of(borscht()));
        when(orderRepo.nextId()).thenReturn(1);

        OrderDTO result = service.placeOrder(1, List.of("Піца Маргарита", "Борщ"));

        assertEquals(275.0, result.getTotalPrice());
        assertEquals(2, result.getDishNames().size());
    }

    @Test
    void placeOrder_idIncrements_onEachOrder() {
        when(customerRepo.findById(1)).thenReturn(Optional.of(ivan()));
        when(dishRepo.findByName("Борщ")).thenReturn(List.of(borscht()));
        when(orderRepo.nextId()).thenReturn(1, 2);

        OrderDTO first = service.placeOrder(1, List.of("Борщ"));
        OrderDTO second = service.placeOrder(1, List.of("Борщ"));

        assertNotEquals(first.getOrderId(), second.getOrderId());
    }

    // --- Сценарій 2: Скасування замовлення ---

    @Test
    void cancelOrder_success() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(pendingOrder()));

        OrderDTO result = service.cancelOrder(1);

        assertEquals("CANCELLED", result.getStatus());
    }

    @Test
    void cancelOrder_alreadyCancelled_throws() {
        Order order = pendingOrder();
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> service.cancelOrder(1));
    }

    @Test
    void cancelOrder_notFound_throws() {
        when(orderRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.cancelOrder(999));
    }

    @Test
    void cancelOrder_completedOrder_throws() {
        Order order = pendingOrder();
        order.setStatus(OrderStatus.COMPLETED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> service.cancelOrder(1));
    }

    // --- Сценарій 3: Завершення замовлення ---

    @Test
    void completeOrder_success() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(pendingOrder()));

        OrderDTO result = service.completeOrder(1);

        assertEquals("COMPLETED", result.getStatus());
    }

    @Test
    void completeOrder_alreadyCompleted_throws() {
        Order order = pendingOrder();
        order.setStatus(OrderStatus.COMPLETED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> service.completeOrder(1));
    }

    @Test
    void completeOrder_alreadyCancelled_throws() {
        Order order = pendingOrder();
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> service.completeOrder(1));
    }

    @Test
    void completeOrder_notFound_throws() {
        when(orderRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.completeOrder(99));
    }

    // --- Сценарій 4: Пошук страв ---

    @Test
    void findDishesByName_returnsMatches() {
        when(dishRepo.findByName("Піца")).thenReturn(List.of(pizza()));

        List<Dish> result = service.findDishesByName("Піца");

        assertEquals(1, result.size());
    }

    @Test
    void findDishesByName_noMatches_returnsEmpty() {
        when(dishRepo.findByName("Суші")).thenReturn(List.of());

        List<Dish> result = service.findDishesByName("Суші");

        assertTrue(result.isEmpty());
    }

    @Test
    void findDishesByName_blankQuery_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findDishesByName("  "));
    }

    // --- Сценарій 5: Реєстрація клієнта ---

    @Test
    void registerCustomer_success() {
        when(customerRepo.existsById(2)).thenReturn(false);

        CustomerDTO result = service.registerCustomer(2, "Марія");

        assertEquals(2, result.getId());
        assertEquals("Марія", result.getName());
        verify(customerRepo).save(any(Customer.class));
    }

    @Test
    void registerCustomer_duplicateId_throws() {
        when(customerRepo.existsById(1)).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> service.registerCustomer(1, "Дублікат"));
    }

    @Test
    void registerCustomer_blankName_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerCustomer(3, "  "));
    }
}
