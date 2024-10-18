> # Упрощенная CRM-система

***

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/ed4ed700dcc55b2c1f1c)

***

## ⭐️ Описание

Данная CRM-система предназначена для управления информацией о продавцах и их транзакциях. 
Система предоставляет RESTful API для выполнения операций CRUD над сущностями Продавец и Транзакция, 
а также включает аналитические функции для обработки и анализа данных.

***

## 📎  Стек технологий

- **Язык программирования:** `Java`
- **Фреймворк:** `Spring Boot`
- **База данных:** `PostgreSQL` (для разработки и продакшена), `H2` (для тестирования в памяти)
- **ORM:** `Spring Data JPA`
- **Сборщик:** `Gradle (Kotlin)`
- **Тестирование:** `JUnit` `Mockito`

***

## 🔩 Функциональность

### 👺 **Продавцы:**

- Получение информации о всех продавцах.
- Получение информации о конкретном продавце по его идентификатору.
- Добавление нового продавца.
- Обновление информации о существующем продавце.
- Удаление продавца.

***

### 💳 **Транзакции:**

- Получение всех транзакций.
- Получение информации о конкретной транзакции по идентификатору.
- Получение информации о конкретной транзакции по идентификатору продавца.
- Добавление новой транзакции для продавца.

***

## ➰ Зависимости

Проект основан на Java и использует следующие основные зависимости:

- `Java 17`: Убедитесь, что у вас установлена версия Java 17 или выше.
- `Spring Boot 3.x`: Используется для создания RESTful API и управления зависимостями.
- `Gradle (Kotlin)`: Для управления зависимостями и сборки проекта.
- `H2 Database`: Встроенная база данных для тестирования.
- `Spring Data JPA`: Для работы с базой данных.
- `JUnit 5`: Для написания и выполнения тестов.
- `Mockito`: Для создания моков в тестах.

***

## 💿 Установка и запуск

### 🔧 Предварительные требования

- `Java 17+`
- `PostgreSQL`

***

### 📑 Шаги

1. **Клонирование репозитория:** \
    Клонировать репозиторий приложения можно здесь:\
    [![Open in GitHub](https://gist.githubusercontent.com/cxmeel/0dbc95191f239b631c3874f4ccf114e2/raw/bb4634715f95ebb209b4e0bcdd4d2d98fe64c64c/github-icon.svg)](https://github.com/mercy191?tab=repositories/)

2. **Создание базы данных:** \
    Необходимо создать базу данных на основе `PostgreSQL`. 
    При необходимости создайте нового пользователя базы данных.
    Если же база данных уже существует, то пропустите этот шаг.

3. **Настройка параметров подключения:** \
    Перейдите в файл `src/main/resources/application.properties`. 
    Обновите параметры подключения согласно вашей базе данных:

    ```properties
    spring.datasource.url=jdbc:postgresql://your_url/your_db_name
    spring.datasource.username=your_user_name
    spring.datasource.password=your_user_password
    ```
   
4. **Сборка и запуск:** \
    Соберите и запустите приложение. Проверьте сборку на наличие ошибок.

***

## 🔎 **Примеры использования API**

### 👺 **Продавцы**

- **Получение списка всех продавцов**:
    - **URL:**: `GET /api/springboot_crm/sellers`  
    - **Пример запроса:**  
      ```bash
      curl -X GET http://localhost:8080/api/springboot_crm/sellers
      ```
- **Получение информации о продавце по ID:**
    - **URL:** `GET /api/springboot_crm/sellers/{id}`
    - **Пример запроса:**  
        ```bash
        curl -X GET http://localhost:8080/api/springboot_crm/sellers/1
        ```
- **Получение самого продуктивного продавца:**
    - **URL:**: `GET /api/springboot_crm/sellers/mostProductive`
    - **Параметры запроса:** `start` `end`
    - **Пример запроса:**
      ```bash
      curl -X GET "http://localhost:8080/api/springboot_crm/sellers/mostProductive?start=2023-01-01T00:00:00&end=2024-12-31T23:59:59"
      ```
- **Получение списка продавцов с суммой меньше указанной:**
    - **URL:**: `GET /api/springboot_crm/sellers/withTotalAmountLessThan`
    - **Параметры запроса:** `amount` `strart` `end`
    - **Пример запроса:**
      ```bash
      curl -X GET "http://localhost:8080/api/springboot_crm/sellers/withTotalAmountLessThan?amount=1000&start=2023-01-01T00:00:00&end=2024-12-31T23:59:59"
      ```
- **Создание нового продавца:**
    - **URL:** `POST /api/springboot_crm/sellers`
    - **Тело запроса:**
      ```json
      {
        "name": "New Seller",
        "contactInfo": "newseller@example.com"
      }
      ```
    - **Пример запроса:**
      ```bash
      curl -X POST -H "Content-Type: application/json" -d 
      '{
        "name": "New Seller",
        "contactInfo": "newseller@example.com"
      }' http://localhost:8080/api/springboot_crm/sellers
      ```
- **Обновление информации о продавце:**
    - **URL:** `PUT /api/springboot_crm/sellers/{id}`
    - **Тело запроса:**
      ```json
      {
        "name": "Updated Seller",
        "contactInfo": "updatedseller@example.com"
      }
      ```
    - **Пример запроса:**
      ```bash
      curl -X PUT -H "Content-Type: application/json" -d 
      '{
        "name": "Updated Seller",
        "contactInfo": "updatedseller@example.com"
      }' http://localhost:8080/api/springboot_crm/sellers/1
      ```
- **Удаление информации о продавце:**
    - **URL:**: `DELETE /api/springboot_crm/sellers/{id}`
    - **Пример запроса:**
      ```bash
      curl -X DELETE http://localhost:8080/api/springboot_crm/sellers/1
      ```
        
***

### 💳 Транзакции

- **Получение списка всех транзакций:**
  - **URL:**: `GET /api/springboot_crm/transactions`
  - **Пример запроса:**
    ```bash
    curl -X GET http://localhost:8080/api/springboot_crm/transactions
    ```
- **Получение информации о транзакции по ID:**
    - **URL:**: `GET /api/springboot_crm/transactions/{id}`
    - **Пример запроса:**
      ```bash
      curl -X GET http://localhost:8080/api/springboot_crm/transactions/1
      ```
- **Получение списка транзакций по ID продавца:**
    - **URL:**: `GET /api/springboot_crm/transactions/sellerId/{sellerId}`
    - **Пример запроса:**
      ```bash
      curl -X GET http://localhost:8080/api/springboot_crm/transactions/sellerId/1
      ```
- **Добавление новой транзакции:**
    - **URL:** `POST /api/springboot_crm/transactions`
    - **Параметры запроса:** `sellerId`
    - **Тело запроса:**
      ```json
      {
        "amount": 1500.00,
        "paymentType": "CARD"
      }
      ```
    - **Пример запроса:**
      ```bash
      curl -X POST -H "Content-Type: application/json" -d '{
      "amount": 1500.00,
      "paymentType": "CARD"
      }' http://localhost:8080/api/springboot_crm/transactions?sellerId=1
      ```
         
***

## 🔧 **Разработка и тестирование**

Для запуска тестов используйте следующую команду:
```bash
mvn test
```
  
***

## ©️ **Лицензия**

Данный проект распространяется под лицензией MIT. \
[![Software License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](LICENSE.txt)

***