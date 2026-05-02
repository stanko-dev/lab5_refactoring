# Restaurant Service Layer

Java-система управління рестораном, побудована за архітектурою Controller-Service-Repository з повним покриттям юніт-тестами.

## Бізнес-сценарії

**1. Розміщення замовлення — `placeOrder(customerId, dishNames)`**  
Клієнт обирає страви зі списку. Сервіс перевіряє існування клієнта та кожної страви, створює замовлення зі статусом `PENDING` і зберігає його в репозиторії.

**2. Скасування замовлення — `cancelOrder(orderId)`**  
Переводить замовлення у статус `CANCELLED`. Якщо замовлення вже скасовано або завершено — кидає виняток.

**3. Пошук страв за назвою — `findDishesByName(name)`**  
Повертає всі страви, назва яких містить пошуковий рядок (регістронезалежно). Порожній запит — виняток.

**4. Реєстрація клієнта — `registerCustomer(id, name)`**  
Додає нового клієнта. Дублікат id або порожнє ім'я — виняток.

## Структура

```
src/main/java/com/restaurant/
├── model/       — Dish, Customer, Order, OrderStatus
├── dto/         — OrderDTO, CustomerDTO
├── repository/  — OrderRepository, CustomerRepository, DishRepository (інтерфейси + InMemory-реалізації)
├── service/     — RestaurantService (бізнес-логіка)
└── controller/  — RestaurantController (точка входу)

src/test/java/com/restaurant/
└── RestaurantServiceTest.java — 13 юніт-тестів
```

## Запуск тестів

```bash
mvn test
```

```
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS
```

## Перевірка стилю коду

```bash
mvn checkstyle:check
```
