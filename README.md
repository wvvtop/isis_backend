```markdown
# Backend — Spring Boot

Это серверная часть приложения (API).

## Технологический стек

- **Java 17 - совместимо с версиец 21**
- **Spring Boot**
- Maven
- Spring Data JPA + Hibernate
- Spring Security + JWT
- Springdoc OpenAPI (Swagger UI)
- PostgreSQL


## Быстрый запуск

### 1. Перейди в папку backend
```bash
cd backend
```


### 2. Запуск в режиме разработки
```bash
# С профилем dev (рекомендуется)
./mvnw spring-boot:run
```

Приложение запустится на: **http://localhost:8081**

### 3. Сборка JAR-файла
```bash
./mvnw clean package
# или с профилем prod
./mvnw clean package -Pprod
```

Готовый файл: `target/*.jar`

### Запуск собранного JAR
```bash
java -jar target/название-приложения-0.0.1.jar --spring.profiles.active=prod
```

## Конфигурация

### Важный файл
Замени в application.properties данные, которые отделньо написаны
### Как настроить продакшен

1. Скопируй шаблон:
   ```bash
   cp src/main/resources/application-prod.yml.example \
      src/main/resources/application-prod.yml
   ```

2. Открой `application-prod.yml` и заполни реальные значения.

**Рекомендуемый способ (лучше)** — использовать переменные окружения:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`

## Полезные команды Maven

```bash
./mvnw clean compile          # компиляция
./mvnw test                   # тесты
./mvnw spring-boot:run        # запуск
./mvnw clean package          # сборка JAR
./mvnw dependency:tree        # дерево зависимостей
```
