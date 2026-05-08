<div align="center">

# 🍽️ Restaurant Service Layer

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-3.8-C71A36?style=flat-square&logo=apachemaven)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162?style=flat-square&logo=junit5)
![Mockito](https://img.shields.io/badge/Mockito-5-green?style=flat-square)
![Tests](https://img.shields.io/badge/Tests-34%20passed-brightgreen?style=flat-square)
![Checkstyle](https://img.shields.io/badge/Checkstyle-passing-brightgreen?style=flat-square)

Java-система управління рестораном з архітектурою **Controller → Service → Repository**,  
повним покриттям юніт-тестів (Mockito) та перевіркою стилю коду.

</div>

---

## 📐 Архітектура

```
RestaurantController          ← точка входу, делегує виклики
        │
        ▼
RestaurantService             ← вся бізнес-логіка та валідація
        │
   ┌────┴────────────┐
   ▼                 ▼
OrderRepository   CustomerRepository   DishRepository
   │                 │                      │
InMemory...       InMemory...           InMemory...    ← реалізації
```

Залежності інжектуються через конструктор — `Service` знає тільки про інтерфейси репозиторіїв.

---

## 🗂️ Структура проєкту

```
src/
├── main/java/com/restaurant/
│   ├── Main.java           ← CLI точка входу (mvn exec:java)
│   ├── model/          Dish · Customer · Order · OrderStatus
│   ├── dto/            OrderDTO · CustomerDTO
│   ├── repository/     інтерфейси + InMemory-реалізації
│   ├── service/        RestaurantService  ← бізнес-логіка
│   └── controller/     RestaurantController
│
└── test/java/com/restaurant/
    ├── RestaurantServiceTest.java     (20 тестів, Mockito)
    └── RestaurantRepositoryTest.java  (14 тестів)
```

---

## ⚙️ Бізнес-сценарії

| №  | Метод | Опис |
|----|-------|------|
| 1  | `placeOrder(customerId, dishNames)` | Перевіряє клієнта та страви, створює замовлення зі статусом `PENDING` |
| 2  | `cancelOrder(orderId)` | Переводить у `CANCELLED`; виняток якщо вже скасовано або завершено |
| 3  | `completeOrder(orderId)` | Переводить у `COMPLETED`; виняток якщо вже завершено або скасовано |
| 4  | `findDishesByName(name)` | Пошук за підрядком (регістронезалежно); порожній запит — виняток |
| 5  | `registerCustomer(id, name)` | Реєструє клієнта; дублікат id або порожнє ім'я — виняток |

---

## 🚀 Швидкий старт

**Вимоги:** Java 21+, Maven 3.8+

```bash
# Клонувати репозиторій
git clone https://github.com/stanko-dev/lab5_refactoring.git
cd lab5_refactoring

# Запустити тести
mvn test

# Запустити CLI демо (всі 5 сценаріїв)
mvn exec:java

# Перевірка стилю коду
mvn checkstyle:check
```

---

## 🧪 Тести

```
Tests run: 34, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

### RestaurantServiceTest — 20 тестів (з Mockito)

Сервісні тести ізолюють `RestaurantService` через **мок-репозиторії** (`@Mock` + `@InjectMocks`).  
Жодна реальна реалізація репозиторію не використовується — перевіряється лише бізнес-логіка.

| Сценарій | Тести |
|----------|-------|
| `placeOrder` | success · customerNotFound · emptyDishes · unknownDish · multipleDishes · idIncrements |
| `cancelOrder` | success · alreadyCancelled · notFound · completedOrder |
| `completeOrder` | success · alreadyCompleted · alreadyCancelled · notFound |
| `findDishesByName` | returnsMatches · noMatches · blankQuery |
| `registerCustomer` | success · duplicateId · blankName |

### RestaurantRepositoryTest — 14 тестів

| Репозиторій | Тести |
|-------------|-------|
| `InMemoryOrderRepository` | save+findById · findById missing · findAll · deleteById · unmodifiableCopy |
| `InMemoryCustomerRepository` | save+findById · findById missing · existsById true · existsById false · duplicateId |
| `InMemoryDishRepository` | findAll · findByName substring · findByName caseInsensitive · findByName blank |
