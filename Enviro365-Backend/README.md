# Enviro365 Investments - Junior Developer Assessment

## Candidate
- **Name:** Sithembiso Innocent Kunene


## Project Overview
A full-stack investment withdrawal management system built 
with Spring Boot and React.

## Tech Stack
- Backend: Spring Boot 3.5.14, Java 17
- Database: H2 (in-memory)
- Frontend: React.js
- Testing: JUnit 5, Mockito

## Setup Instructions

### Backend
1. Make sure Java 17+ is installed
2. Open CMD and navigate to project folder:
   cd C:\Users\Cash\Documents\NetBeansProjects\innocent
3. Run: mvnw.cmd spring-boot:run
4. Backend runs at: http://localhost:8080
5. H2 Console: http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:enviro365db
   - Username: sa
   - Password: (empty)

### Frontend
1. Make sure Node.js is installed
2. Open CMD and navigate to frontend folder:
   cd C:\Users\Cash\Documents\enviro365-frontend
3. Run: npm install
4. Run: npm start
5. Frontend runs at: http://localhost:3000

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/investors/{id}/portfolio | Get investor portfolio |
| POST | /api/withdrawals | Submit withdrawal |
| GET | /api/withdrawals?investorId={id} | Get withdrawal history |
| GET | /api/withdrawals/export?investorId={id} | Download CSV |

## Business Rules
- Retirement withdrawals only allowed if investor age > 65
- Withdrawal amount cannot exceed account balance
- Withdrawal amount cannot exceed 90% of balance
- Proper error messages returned for all violations

## Advanced Features Implemented
- Global Exception Handling (@RestControllerAdvice)
- DTO Layer (WithdrawalRequest, InvestorPortfolioDTO)
- Input Validation (@Valid, @NotNull, @Positive)
- Unit Tests (JUnit 5 + Mockito) - 4 tests passing
- UI Validation (client-side before API call)

## AI Usage Disclosure
Claude AI (claude.ai) was used to assist with:
- React frontend components

All AI-generated code was reviewed, understood, 
and customised me.

## Screenshots

### Portfolio Dashboard
![Portfolio](../screenshots/portfolio.png)

### Withdrawal Form
![Withdrawal](../screenshots/withdrawal.png)

### Withdrawal Historypng
![History](../screenshots/history.png)

### API Response
![API](../screenshots/api.png)

### H2 Database Console
![H2](../screenshots/h2console.png)
