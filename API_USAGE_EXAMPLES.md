# API Usage Examples

These examples demonstrate how to use the implemented API endpoints.

## Authentication Flow

### 1. Register a new user
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+998901234567",
    "password": "password123",
    "locale": "uz-Latn"
  }'
```

Response:
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiJ9...",
  "refresh_token": "550e8400-e29b-41d4-a716-446655440000",
  "token_type": "Bearer",
  "expires_in": 900000,
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+998901234567",
    "role": "CUSTOMER",
    "locale": "uz-Latn",
    "createdAt": "2024-01-01T12:00:00Z"
  }
}
```

### 2. Login existing user
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 3. Refresh access token
```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refresh_token": "550e8400-e29b-41d4-a716-446655440000"
  }'
```

## Station and Pricing

### 1. Get all active stations
```bash
curl -X GET "http://localhost:8080/api/v1/stations?active=true"
```

Response:
```json
[
  {
    "id": 1,
    "name": "Binkat Central Station",
    "type": "PETROL_STATION",
    "address": "Tashkent, Chilanzar District",
    "latitude": 41.2995,
    "longitude": 69.2401,
    "active": true,
    "createdAt": "2024-01-01T12:00:00Z"
  }
]
```

### 2. Get current pricing for RON 95 petrol
```bash
curl -X GET "http://localhost:8080/api/v1/pricing/current?stationId=1&kind=PETROL&grade=RON_95"
```

Response:
```json
{
  "id": 10,
  "stationId": 1,
  "stationName": "Binkat Central Station",
  "kind": "PETROL",
  "grade": "RON_95",
  "unit": "LITER",
  "basePriceUzs": 12500.00,
  "effectiveFrom": "2024-01-01T00:00:00Z",
  "effectiveTo": null
}
```

## Wallet Operations (Authenticated)

### 1. Get wallet balance
```bash
curl -X GET http://localhost:8080/api/v1/wallet/balance \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

Response:
```json
{
  "walletId": 1,
  "currentBalance": 1250.50,
  "currency": "POINTS",
  "lastUpdated": "2024-01-01T15:30:00Z"
}
```

### 2. Get wallet transaction history
```bash
curl -X GET "http://localhost:8080/api/v1/wallet/ledger?limit=10" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

Response:
```json
{
  "entries": [
    {
      "id": 25,
      "reason": "REFILL_EARNED",
      "pointsChange": 125.00,
      "balanceAfter": 1250.50,
      "description": "Points earned from refill at Binkat Central Station",
      "refillTransactionId": 15,
      "expiresAt": "2025-01-01T12:00:00Z",
      "createdAt": "2024-01-01T12:00:00Z"
    }
  ],
  "totalCount": 25,
  "hasMore": true,
  "nextCursor": "1"
}
```

## Refill Transactions (Authenticated)

### 1. Create a new refill transaction
```bash
curl -X POST http://localhost:8080/api/v1/refills \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "stationId": 1,
    "kind": "PETROL",
    "grade": "RON_95",
    "quantity": 25.5,
    "notes": "Full tank"
  }'
```

Response:
```json
{
  "id": 15,
  "userId": 1,
  "stationId": 1,
  "stationName": "Binkat Central Station",
  "kind": "PETROL",
  "grade": "RON_95",
  "unit": "LITER",
  "quantity": 25.5,
  "unitPriceUzs": 12500.00,
  "totalAmountUzs": 318750.00,
  "pointsEarned": 31.87,
  "status": "COMPLETED",
  "notes": "Full tank",
  "createdAt": "2024-01-01T12:00:00Z"
}
```

### 2. Get refill history
```bash
curl -X GET "http://localhost:8080/api/v1/refills?limit=10" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## Admin Analytics (Admin Role Required)

### 1. Get analytics overview
```bash
curl -X GET http://localhost:8080/api/v1/admin/analytics/overview \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

Response:
```json
{
  "totalCustomers": 1500,
  "totalRefills": 45000,
  "totalRevenue": 1250000000.00,
  "totalPointsIssued": 12500000.00,
  "totalPointsRedeemed": 2500000.00,
  "lastUpdated": "2024-01-01T12:00:00Z"
}
```

### 2. Get top customers
```bash
curl -X GET http://localhost:8080/api/v1/admin/analytics/top-customers \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## Error Handling Examples

### 1. Validation Error
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "invalid-email",
    "password": "123"
  }'
```

Response (400 Bad Request):
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input",
  "fieldErrors": {
    "name": "must not be blank",
    "email": "must be a well-formed email address",
    "password": "size must be between 6 and 100"
  }
}
```

### 2. Authentication Error
```bash
curl -X GET http://localhost:8080/api/v1/wallet/balance \
  -H "Authorization: Bearer invalid-token"
```

Response (401 Unauthorized):
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid credentials"
}
```

### 3. Resource Not Found
```bash
curl -X GET "http://localhost:8080/api/v1/pricing/current?stationId=999&kind=PETROL&grade=RON_95"
```

Response (400 Bad Request):
```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "No current pricing found for station 999, kind PETROL, grade RON_95"
}
```

## Notes

1. All endpoints require the base URL: `http://localhost:8080/api/v1`
2. Authenticated endpoints require `Authorization: Bearer <access_token>` header
3. Admin endpoints require user with `ADMIN` role
4. Content-Type should be `application/json` for POST requests
5. The application also provides Swagger UI at: `http://localhost:8080/swagger-ui.html`