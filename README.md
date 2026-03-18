
##  Сборка и развертывание

Перед запуском контейнеров необходимо собрать каждый микросервис

```bash
./gradlew bootJar
```


### 2\. Запуск через Docker Compose

```bash
docker-compose up -d --build
```

### Порядок запуска (Startup Order)

1.  **RabbitMQ**
2.  **Config Server**
3.  **Eureka Server**
4.  **API Gateway**
5.  **Остальные сервисы** 

### 1\. Отели (Hotel Service / Catalog 9000 порт)
| Название | Метод | Путь | Пример Body / Параметры |
| :--- | :--- | :--- | :--- |
| **All Hotels** | `GET` | `/hotels` | `sortBy=rating&direction=asc` |
| **Create Hotel** | `POST` | `/hotels` | `{"title": "Radisson Blu", "city": "Batumi", "address": "1 Ninoshvili", "rating": 4.1}` |
| **Update Hotel** | `PATCH` | `/hotels/{id}` | `{"title": "New Hotel Name"}` |
| **Get by ID** | `GET` | `/hotels/{id}` | *Нет* |
| **Delete Hotel** | `DELETE` | `/hotels/{id}` | *Нет* |
| **Get Cities** | `GET` | `/cities` | *Нет (список всех городов)* |

### 2\. Типы номеров (Room Types)

| Название | Метод | Путь | Пример Body |
| :--- | :--- | :--- | :--- |
| **All Types** | `GET` | `/room-types` | *Нет* |
| **Create Type** | `POST` | `/hotels/{id}/room-types` | `{"name": "Econom", "basePrice": 50.0, "capacity": 2.0}` |
| **Update Type** | `PATCH` | `/room-types/{id}` | *Нет (обновление по ID)* |
| **Get by ID** | `GET` | `/room-types/{id}` | *Нет* |
| **Delete Type** | `DELETE` | `/room-types/{id}` | *Нет* |
| **Add Amenities**| `PATCH` | `/room-types/{id}/amenities`| `["uuid-1", "uuid-2"]` (Массив ID) |
| **Rem. Amenities**| `DELETE` | `/room-types/{id}/amenities`| `["uuid-1"]` (Массив ID на удаление) |

### 3\. Удобства (Amenities)

| Название | Метод | Путь | Пример Body |
| :--- | :--- | :--- | :--- |
| **All Amenities** | `GET` | `/amenities` | *Нет* |
| **Create Amenity**| `POST` | `/amenities` | `{"name": "Animal friendly", "category": "Pets"}` |
| **Get by ID** | `GET` | `/amenities/{id}` | *Нет* |
| **Delete Amenity**| `DELETE` | `/amenities/{id}` | *Нет* |

### 4\. Пользователи (Auth Service 9002)

| Название | Метод | Путь | Пример Body |
| :--- | :--- | :--- | :--- |
| **Login** | `POST` | `/login` | `{"username": "testA", "password": "123test"}` |
| **Register** | `POST` | `/register` | `{"username": "testA", "password": "123testT"}` |
| **Promote User** | `PATCH` | `/promote/{id}` | `{"username": "test", "password": "..."}` |

### 5\. Бронирование (Booking Service 9001)

| Название | Метод | Путь | Пример Body |
| :--- | :--- | :--- | :--- |
| **Create Booking**| `POST` | `/bookings` | `{"roomTypeId": "uuid", "checkIn": "2026-03-05", "checkOut": "2026-03-10"}` |
| **My Bookings** | `GET` | `/bookings/my` | *Нет (фильтр по токену пользователя)* |
| **Cancel Booking**| `PATCH` | `/bookings/{id}/cancel`| `{"roomTypeId": "uuid", "checkIn": "...", "checkOut": "..."}` |

