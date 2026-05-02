package com.restaurant;

import com.restaurant.model.Customer;
import com.restaurant.model.Dish;
import com.restaurant.model.Order;
import com.restaurant.repository.InMemoryCustomerRepository;
import com.restaurant.repository.InMemoryDishRepository;
import com.restaurant.repository.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantRepositoryTest {

    @Nested
    class OrderRepositoryTests {
        private InMemoryOrderRepository repo;
        private Customer customer;
        private Dish dish;

        @BeforeEach
        void setUp() {
            repo = new InMemoryOrderRepository();
            customer = new Customer(1, "Тест");
            dish = new Dish("Піца", 150.0, "Піца");
        }

        @Test
        void save_findById_returnsOrder() {
            Order order = new Order(1, customer, List.of(dish));
            repo.save(order);
            assertTrue(repo.findById(1).isPresent());
        }

        @Test
        void findById_missing_returnsEmpty() {
            assertTrue(repo.findById(99).isEmpty());
        }

        @Test
        void findAll_returnsAllSaved() {
            repo.save(new Order(1, customer, List.of(dish)));
            repo.save(new Order(2, customer, List.of(dish)));
            assertEquals(2, repo.findAll().size());
        }

        @Test
        void deleteById_removesOrder() {
            repo.save(new Order(1, customer, List.of(dish)));
            repo.deleteById(1);
            assertTrue(repo.findById(1).isEmpty());
        }

        @Test
        void findAll_returnsUnmodifiableCopy() {
            repo.save(new Order(1, customer, List.of(dish)));
            List<Order> all = repo.findAll();
            assertThrows(UnsupportedOperationException.class,
                    () -> all.add(new Order(2, customer, List.of(dish))));
        }
    }

    @Nested
    class CustomerRepositoryTests {
        private InMemoryCustomerRepository repo;

        @BeforeEach
        void setUp() {
            repo = new InMemoryCustomerRepository();
        }

        @Test
        void save_findById_returnsCustomer() {
            repo.save(new Customer(1, "Іван"));
            assertTrue(repo.findById(1).isPresent());
            assertEquals("Іван", repo.findById(1).get().getName());
        }

        @Test
        void findById_missing_returnsEmpty() {
            assertTrue(repo.findById(99).isEmpty());
        }

        @Test
        void existsById_true_whenPresent() {
            repo.save(new Customer(1, "Іван"));
            assertTrue(repo.existsById(1));
        }

        @Test
        void existsById_false_whenAbsent() {
            assertFalse(repo.existsById(42));
        }

        @Test
        void save_duplicateId_throws() {
            repo.save(new Customer(1, "Іван"));
            assertThrows(IllegalArgumentException.class,
                    () -> repo.save(new Customer(1, "Петро")));
        }
    }

    @Nested
    class DishRepositoryTests {
        private InMemoryDishRepository repo;

        @BeforeEach
        void setUp() {
            repo = new InMemoryDishRepository();
            repo.save(new Dish("Піца Маргарита", 180.0, "Піца"));
            repo.save(new Dish("Піца Пепероні", 210.0, "Піца"));
            repo.save(new Dish("Борщ", 95.0, "Супи"));
        }

        @Test
        void findAll_returnsAllDishes() {
            assertEquals(3, repo.findAll().size());
        }

        @Test
        void findByName_matchesSubstring() {
            assertEquals(2, repo.findByName("Піца").size());
        }

        @Test
        void findByName_caseInsensitive() {
            assertEquals(2, repo.findByName("піца").size());
        }

        @Test
        void findByName_blankQuery_returnsEmpty() {
            assertTrue(repo.findByName("  ").isEmpty());
        }
    }
}
