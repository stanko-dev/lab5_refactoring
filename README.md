<div align="center">

# 🍽️ Restaurant Service Layer

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-3.8-C71A36?style=flat-square&logo=apachemaven)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162?style=flat-square&logo=junit5)
![Tests](https://img.shields.io/badge/Tests-13%20passed-brightgreen?style=flat-square)
![Checkstyle](https://img.shields.io/badge/Checkstyle-passing-brightgreen?style=flat-square)

Java-система управління рестораном з архітектурою **Controller → Service → Repository**,  
повним покриттям юніт-тестів та перевіркою стилю коду.

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
│   ├── model/          Dish · Customer · Order · OrderStatus
│   ├── dto/            OrderDTO · CustomerDTO
│   ├── repository/     інтерфейси + InMemory-реалізації
│   ├── service/        RestaurantService  ← бізнес-логіка
│   └── controller/     RestaurantController
│
└── test/java/com/restaurant/
    └── RestaurantServiceTest.java   (13 тестів)
```

---

## ⚙️ Бізнес-сценарії

| №  | Метод | Опис |
|----|-------|------|
| 1  | `placeOrder(customerId, dishNames)` | Перевіряє клієнта та страви, створює замовлення зі статусом `PENDING` |
| 2  | `cancelOrder(orderId)` | Переводить у `CANCELLED`; кидає виняток якщо вже скасовано або завершено |
| 3  | `findDishesByName(name)` | Пошук за підрядком (регістронезалежно); порожній запит — виняток |
| 4  | `registerCustomer(id, name)` | Реєструє клієнта; дублікат id або порожнє ім'я — виняток |

---

## 🚀 Швидкий старт

**Вимоги:** Java 21+, Maven 3.8+

```bash
# Клонувати репозиторій
git clone https://github.com/stanko-dev/lab5_refactoring.git
cd lab5_refactoring

# Запустити тести
mvn test

# Перевірка стилю коду
mvn checkstyle:check
```

---

## 🧪 Тести

```
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

| Сценарій | Тести |
|----------|-------|
| `placeOrder` | success · customerNotFound · emptyDishes · unknownDish |
| `cancelOrder` | success · alreadyCancelled · notFound |
| `findDishesByName` | returnsMatches · noMatches · blankQuery |
| `registerCustomer` | success · duplicateId · blankName |
