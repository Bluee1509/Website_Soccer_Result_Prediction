# AGENTS.md - WC2026 Football Betting Platform

## Project Overview

**WC2026** is a Spring Boot 4.0.6 REST API for World Cup 2026 football betting platform. Currently implements user registration with Vietnamese phone number normalization. Future phases will add betting engine, odds management, and transaction ledger.

**Tech Stack**: Java 17, Spring Boot 4.0.6, PostgreSQL, Flyway migrations, Lombok, Jakarta Validation

---

## Architecture & Data Flow

### 4-Layer Pattern (Controller → Service → Repository → Model)

**Example: User Registration Flow**
```
POST /api/users/register
  ↓ [UserController]
  → Validates @Valid UserRequest (regex, email, size)
  ↓ [UserService.registerUser()]
  → Normalizes phone: "+84912345678" → "0912345678"
  → Checks existsByUsername() & existsByEmail()
  → Creates User with balance=0, role="USER"
  ↓ [UserRepository.save()]
  → Persists to PostgreSQL via JPA
  ← Returns User JSON
```

### Database Schema (4 Migrations)

| V1 | `users` | Core user data (username as normalized phone, password plaintext, email, balance, role) |
| V2 | `teams`, `matches` | Teams (name, logo_url), Matches (home_team, away_team, start_time, status, scores) |
| V3 | `odds`, `bet_tickets` | Odds (match_id, odd_type, choice, rate), BetTickets (user_id, match_id, status, amount, potential_win) |
| V4 | `transactions` | Transaction ledger (user_id, amount, type: DEPOSIT/WITHDRAW/BET_PLACED/BET_WON) |

---

## Key Conventions & Patterns

### Vietnamese Phone Normalization (Critical Business Logic)

**See:** `UserService.registerUser()` - normalizes phone input to canonical form `0XXXXXXXXX` before DB uniqueness check.

**Pattern:** Different input formats (`+84`, `84`, `0`) all map to single normalized form to prevent duplicates. **Important:** Normalization must happen in Service layer BEFORE calling `existsByUsername()`.

### DTO Validation Pattern

**See:** `UserRequest.java` - uses Jakarta `@Pattern` annotation with Vietnamese regex for phone validation.

**Pattern:** Vietnamese phone regex: `^(0|\\+84)(3|5|7|8|9)\\d{8}$` enforces 0 or +84 prefix + carrier digit (3/5/7/8/9) + 8 digits. Error messages in Vietnamese.

### Global Exception Handler

`GlobalExceptionHandler.java` catches `MethodArgumentNotValidException` and returns **first field error message** as status 400. Vietnamese error messages defined in @Pattern annotations.

### Entity Design Notes

- **User.java**: BigDecimal for balance (never float!), role hardcoded to "USER" on registration
- **Passwords**: Currently **plaintext** - no hashing implemented yet
- **Username**: Actually stores normalized phone, not traditional username

---

## Build & Development Commands

```bash
# Build project
mvnw clean package

# Run application (hot reload enabled via devtools)
mvnw spring-boot:run

# Run tests
mvnw test

# View compiled SQL logs
# Configured in application.yml: show-sql: true, format_sql: true
```

### Database Setup

```sql
-- PostgreSQL must be running on localhost:5432
CREATE DATABASE wc2026;
-- Credentials in application.yml:
--   username: postgres
--   password: 15092002
```

Flyway automatically runs migrations on startup (`V1`, `V2`, `V3`, `V4` in order).

---

## Adding New Features: Step-by-Step

### Add a new REST endpoint (e.g., get user profile)

1. **Add Repository method** if querying by non-ID field:
   ```java
   // UserRepository
   Optional<User> findByUsername(String username);
   ```

2. **Add Service method**:
   ```java
   @Service
   public List<User> getAllUsers() {
       return userRepository.findAll();  // Fetch all from DB
   }
   public User getUserProfile(String username) {
       return userRepository.findByUsername(username)
   @GetMapping("/{username}")
   public ResponseEntity<?> getProfile(@PathVariable String username) {
       return ResponseEntity.ok(userService.getUserProfile(username));
   }
   ```


### Add a new database table

1. Create migration file `src/main/resources/db/migration/V5__description.sql`
2. Write CREATE TABLE SQL with constraints, foreign keys, indexes
3. Add JPA Entity class in `model/` package with @Entity, @Table, Lombok annotations
4. Create Repository extending `JpaRepository<Entity, Long>`
5. Create Service class with business logic
6. Create DTO class for request validation if needed
7. Add Controller with @RestController, @RequestMapping endpoints

### Business Logic Rules

- **All money values**: Use `BigDecimal`, not double/float
- **Phone input**: Always normalize before storing (call UserService utility)
- **Balance updates**: Via transactions table, never direct user.setBalance()
- **Validation errors**: Return Vietnamese messages from DTO @Pattern annotations

---

## Critical Developer Notes

### Security Warnings ⚠️

- Passwords stored **plaintext** - implement BCryptPasswordEncoder before production
- Database credentials hardcoded in `application.yml` - use environment variables
- No authentication implemented yet (Spring Security dependency commented out)
- No authorization checks - all endpoints public

### Language Convention

- Code comments in **Vietnamese** (see UserService, UserRepository, GlobalExceptionHandler)
- Error messages in **Vietnamese** (see UserRequest validation messages)
- Commit messages expected in Vietnamese

### Common Pitfalls

- **Phone uniqueness**: Phone normalization happens in Service, not DB constraint - ensure consistency
- **Email vs Phone**: Email has separate unique constraint, both required on registration
- **Role field**: Currently hardcoded to "USER" - future versions will support ADMIN registration differently
- **Bet calculations**: potential_win = amount * rate (verify this in future betting features)

---

## Integration Points & External Systems

### Planned Integrations (from V2-V4 schema)

- **Football data provider**: Populate teams & matches tables
- **Odds calculation engine**: Compute and store odds based on betting patterns
- **Payment gateway**: For DEPOSIT/WITHDRAW transaction types
- **Notification service**: Alert users on bet results (not yet implemented)

### API Response Format

All responses follow Spring Default:
- Success: HTTP 200 + JSON body
- Validation error: HTTP 400 + error message string
- Server error: HTTP 500 (GlobalExceptionHandler catches RuntimeException)

---

## Project Status & Phases

**Phase 1 (Current)**: User registration with phone validation ✅
**Phase 2 (Planned)**: Odds management, bet placement endpoints
**Phase 3 (Planned)**: Match results, automatic settlement
**Phase 4 (Planned)**: Dashboard, reporting, authentication

Odds types placeholder in schema: `1X2` (home win/draw/away), `TAI_XIU` (over/under), `CHAP` (handicap)

---

## Key Files Reference

| File | Purpose |
|------|---------|
| `UserRequest.java` | DTO with phone validation regex - examine for field-level validation pattern |
| `UserService.java` | Phone normalization logic - model for business rule implementation |
| `GlobalExceptionHandler.java` | Error response pattern - follow for new exception handlers |
| `V1__init_users_table.sql` | User schema - reference for new entity migrations |
| `application.yml` | DB connection, Flyway config, JPA show-sql |
| `pom.xml` | Dependencies (no explicit security/auth yet - commented out) |

