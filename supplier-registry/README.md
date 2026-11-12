# Supplier Registry

Небольшое Spring Boot-приложение для проверки доступности телефонного номера у выбранного поставщика.

## Требования

- Java 17+
- Maven 3.8+

## Запуск тестов

```bash
mvn clean test
```

HTTP-запросы в тестах не уходят в сеть: используется `MockRestServiceServer`.

## Запуск приложения

```bash
mvn spring-boot:run
```

При старте поставщики читаются из JSON-файла `./suppliers.json` относительно рабочей директории. Путь можно переопределить:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments='--suppliers.file=/absolute/path/suppliers.json'
```

Пример файла:

```json
[
  {
    "id": "supplier-1",
    "baseUrl": "https://api.supplier.example",
    "apiKey": "secret-key"
  }
]
```

## Использование

```java
boolean available = subscriberService.isAvailable("supplier-1", "79991234567");
```

- `200` с JSON `{"status":"ok"}` возвращает `true`.
- `404` возвращает `false`.
- Другие HTTP-ошибки и сетевые ошибки не подавляются и доходят до вызывающего кода.
- API-ключ передаётся в заголовке `X-API-Key`.

`SupplierRegistry` также можно создать напрямую из готового списка и заменить весь список методом `replaceAll`.

## Структура

- `model` — модели поставщика и ответа поставщика.
- `registry` — потокобезопасный реестр с атомарной полной заменой.
- `provider` — чтение списка поставщиков из JSON.
- `client` — HTTP-клиент поставщика.
- `service` — бизнес-операция проверки номера.
- `config` — конфигурация Spring.
