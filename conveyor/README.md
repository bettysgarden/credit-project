# Микросервис Conveyor

## Описание

Микросервис предоставляет два основных эндпоинта для расчета возможных условий кредита и полной валидации, скоринга и
расчета кредита. Он обрабатывает данные заявки на кредит, проводит скоринг и возвращает потенциальные условия кредита.

## Эндпоинты

### 1. Расчет предложений по кредиту

**POST** `/conveyor/offers`

Выполняет расчет возможных кредитных предложений на основе данных заявки.

- **Запрос:** `LoanApplicationRequestDTO`
- **Ответ:** Список `LoanOfferDTO`

### 2. Полный расчет кредита и скоринг

**POST** `/conveyor/calculation`

Валидация данных, проведение скоринга и возврат полного расчета параметров кредита.

- **Запрос:** `ScoringDataDTO`
- **Ответ:** `CreditDTO`

## Модели данных

### LoanApplicationRequestDTO

- **amount**: `BigDecimal` — Запрашиваемая сумма кредита
- **term**: `Integer` — Срок кредита в месяцах
- **firstName**: `String` — Имя заявителя
- **lastName**: `String` — Фамилия заявителя
- **email**: `String` — Email заявителя
- **birthdate**: `LocalDate` — Дата рождения
- **passportSeries**: `String` — Серия паспорта
- **passportNumber**: `String` — Номер паспорта

### LoanOfferDTO

- **applicationId**: `Long` — ID заявки
- **requestedAmount**: `BigDecimal` — Запрашиваемая сумма кредита
- **totalAmount**: `BigDecimal` — Общая сумма с учетом процентов
- **monthlyPayment**: `BigDecimal` — Ежемесячный платеж
- **rate**: `BigDecimal` — Процентная ставка

### ScoringDataDTO

- **amount**: `BigDecimal` — Запрашиваемая сумма кредита
- **term**: `Integer` — Срок кредита в месяцах
- **employmentDTO**: `EmploymentDTO` — Информация о занятости
- **isInsuranceEnabled**: `Boolean` — Включена ли страховка

### CreditDTO

- **amount**: `BigDecimal` — Сумма кредита
- **term**: `Integer` — Срок кредита
- **monthlyPayment**: `BigDecimal` — Ежемесячный платеж
- **rate**: `BigDecimal` — Процентная ставка
- **paymentSchedule**: Список дат и сумм платежей
