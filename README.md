# NauJava — кинотеатральный сервис

Приложение на Spring Boot для бронирования сеансов кинотеатра с веб‑интерфейсом на Thymeleaf, REST API, JWT‑аутентификацией и административной панелью.

## Стек
- Java 21, Spring Boot 3.5 (Web, Data JPA, Security, Thymeleaf, HATEOAS, Actuator)
- PostgreSQL (боевой профиль), H2 (тесты)
- JWT (jjwt), Lombok
- Swagger/OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
- JavaMelody для мониторинга

## Модель данных
Сущности соответствуют ER‑диаграмме `ER.md`: пользователи (`User`), кинотеатры (`Cinema`), залы (`Hall`), фильмы (`Movie`), сеансы (`Session`), места (`Seat`), бронирования (`Booking`), отчёты (`Report`). Роли: `USER`, `ADMIN`; статус отчёта: `CREATED`/`FINISHED`/`ERROR`; статус оплаты бронирования в `PaymentStatus`.

## Пользовательские сценарии
- Публично: просмотр списка кинотеатров `/`, выбор кинотеатра `/cinemas/{cinemaId}`, просмотр фильмов и сеансов, регистрация `/registration`, вход `/login`.
- Авторизованный пользователь: просмотр свободных мест и бронирование сеанса `/cinemas/{cinemaId}/{movieId}/{sessionId}/booking`, личный кабинет `/cabinet` со списком бронирований, REST API `/api/booking` (создать, оплатить, получить свои бронирования).
- Администратор: веб‑панель `/admin/**` (пользователи, бронирования, кинотеатры, фильмы, залы, сеансы) и REST API под `/api/admin/**`, а также CRUD для доменных сущностей `/api/admin/{cinema|hall|movie|session}`.
- Отчёты: запуск асинхронной генерации `/api/report/generate`, просмотр HTML‑результата `/api/report/{id}`.

## API
- Аутентификация: `POST /api/auth/register`, `POST /api/auth/login` (возвращает JWT).
- Каталог: `GET /api/cinema`, `/api/cinema/{id}`; `GET /api/hall?cinemaId=...`; `GET /api/movie`, `/api/movie/{id}`; `GET /api/session`, `/api/session/{id}`, `/api/session/{id}/seats`.
- Бронирование: `POST /api/booking`, `GET /api/booking`, `GET /api/booking/{id}`, `POST /api/booking/{id}/pay`, `DELETE /api/booking/{id}`.
- Админ‑CRUD: `POST/PATCH/DELETE /api/admin/{cinema|hall|movie|session}`, `GET /api/admin/user`, `GET /api/admin/user/{id}`, `PATCH/DELETE /api/admin/user/{id}`, поиск пользователей по активности/почте/дате.
- Документация: `GET /swagger-ui/index.html`, OpenAPI JSON по `/v3/api-docs`.

## Безопасность
- Spring Security + форма логина, JWT‑фильтр `JwtAuthenticationFilter` для API.
- Доступ без авторизации: главная, страницы кинотеатров/фильмов/сеансов, регистрация, логин, `POST /api/auth/register`, `POST /api/auth/login`.
- Роли: `USER` и `ADMIN`; админ‑маршруты защищены через `hasRole("ADMIN")`.
- JWT‑секрет задаётся свойством `jwt.secret`.

## Мониторинг
- Actuator включён (базовый профиль).
- JavaMelody по `/monitoring` (логин/пароль `admin:admin123`, задаются в `application.properties`).

## Запуск локально
1) Требования: JDK 21, Maven Wrapper (`mvnw`), PostgreSQL 15+ доступный на `localhost:5433`.
2) Создайте БД и пользователя (пример):
   ```sql
   create database java;
   create user admin with password '1q2w3e4r';
   grant all privileges on database java to admin;
   ```
   Или используйте Docker: `docker run --name naujava-pg -e POSTGRES_DB=java -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=1q2w3e4r -p 5433:5432 -d postgres:16`.
3) При необходимости измените `spring.datasource.*` и `jwt.secret` в `src/main/resources/application.properties`.
4) Собрать и запустить:
   - `./mvnw clean package`
   - `./mvnw spring-boot:run` или `java -jar target/demo-0.0.1-SNAPSHOT.jar`
5) Открыть: `http://localhost:8080` (UI), `http://localhost:8080/swagger-ui/index.html` (API), `http://localhost:8080/monitoring` (мониторинг).

## Полезные заметки
- Админ‑пользователь создается при регистрации первого пользователя, остальные пользователи создаются с ролью USER, чтобы поменять их, нужно воспользоваться панелью администратора `/admin/users`.
- При генерации отчёта заложены задержки (имитация долгих расчётов), статус можно опрашивать тем же `GET /api/report/{id}`.

- В файле `ER.md` представлена схема БД