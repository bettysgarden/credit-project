# Проект "Кредитный Конвейер"

Проект построен с использованием следующего стека технологий:

- Java 17
- Spring Boot
- PostgreSQL, FlyBase
- MapStruct
- OpenAPI, SpringDoc, open-api-generator
- Kafka
- Feign Client
- JUnit 5, Mockito
- Maven

## Микросервисы

### Conveyor

Микросервис **Conveyor** реализует бизнес-логику для расчета кредитных предложений и выполнения полного скоринга заявки
на кредит. Основные функции:

- Расчет возможных условий кредита
- Полный расчет параметров кредита с валидацией данных

### Deal
