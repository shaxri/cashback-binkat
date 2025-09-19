# Binkat Oil — Loyalty & Cashback Platform

A comprehensive, production-ready mobile-first web application for Binkat Oil customers to earn bonus points on fuel refills and redeem them from an in-app wallet.

## 🚀 Tech Stack

### Backend
- **Java 17** + **Spring Boot 3.x**
- **Spring Security** (JWT authentication)
- **Spring Data JPA** + **PostgreSQL 16**
- **Liquibase** for database migrations
- **Redis** for caching and sessions
- **MapStruct** for entity mapping
- **OpenAPI 3.1** documentation
- **Docker** containerization

### Frontend
- **Vue 3** + **TypeScript** + **Vite**
- **Pinia** for state management
- **Vue Router** for navigation
- **Tailwind CSS** for styling
- **Vue i18n** for internationalization
- **PWA** capabilities with **Workbox**

### Infrastructure
- **Docker Compose** for local development
- **PostgreSQL 16** database
- **Redis** for caching
- **Nginx** for production

## 🏗 Architecture

```
/backend     - Spring Boot REST API
/frontend    - Vue.js PWA application
/infra       - Docker Compose configuration
/docs        - API documentation & specs
```

## 🎯 Features

### Core Features
- **Multi-fuel support**: Petrol (RON 92, RON 95), Diesel, Propane, EV charging
- **Points system**: Earn points on refills with tier-based multipliers
- **Wallet**: Redeem points for discounts on future purchases
- **Station finder**: Locate nearby fuel stations with real-time pricing
- **Transaction history**: Complete audit trail of all refills and point transactions

### User Roles
- **CUSTOMER**: Earn and redeem points, view transactions
- **OPERATOR**: Process refills at POS terminals
- **ADMIN**: Analytics dashboard with comprehensive reporting

### Admin Analytics
- Top customers by refills and volume
- Revenue and points analytics
- Petrol grade breakdowns (RON 92/95)
- Propane pressure telemetry (optional)
- Export capabilities (CSV/Excel)

### Localization
- **Languages**: Uzbek Latin (default), Russian, English
- **Currency**: UZS with proper formatting
- **Timezone**: Asia/Tashkent
- **RTL-ready** UI scaffolding

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- Node.js 18+ (for frontend development)
- Java 17+ (for backend development)

### Local Development

1. **Clone the repository**
```bash
git clone https://github.com/shaxri/cashback-binkat.git
cd cashback-binkat
```

2. **Set up environment**
```bash
cp .env.example .env
# Edit .env with your local configuration
```

3. **Start infrastructure services**
```bash
cd infra
docker-compose up -d postgres redis
```

4. **Run backend**
```bash
cd backend
./mvnw spring-boot:run
```

5. **Run frontend**
```bash
cd frontend
npm install
npm run dev
```

6. **Access the application**
- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api/v1
- API Documentation: http://localhost:8080/swagger-ui.html

### Full Docker Setup

```bash
cd infra
docker-compose up -d
```

## 📊 Database Schema

### Core Entities
- **Users** - Customer accounts with roles and tiers
- **Stations** - Fuel stations with location and type info
- **PumpOrCharger** - Individual pumps/chargers with fuel grades
- **PricePlan** - Dynamic pricing by station, fuel type, and grade
- **RefillTransaction** - Complete transaction records
- **PointsLedger** - Immutable points earning/spending history
- **Wallet** - Cached points balance per user
- **Tier** - SILVER/GOLD/PLATINUM with multipliers

### Petrol Grade Support
- **RON 92** and **RON 95** grades for petrol fuel
- Grade-specific pricing and points rules
- Validation ensures grades only apply to petrol

## 🔐 Authentication & Security

- **JWT** access tokens (15 min) + refresh tokens (7 days)
- **Role-based access control** (RBAC)
- **Rate limiting** on auth endpoints
- **Audit logging** for all wallet operations
- **Input validation** with Bean Validation
- **SQL injection protection** via JPA

## 🌍 API Documentation

Complete OpenAPI 3.1 specification available at `/swagger-ui.html`

### Key Endpoints

#### Authentication
```
POST /api/v1/auth/login
POST /api/v1/auth/register
POST /api/v1/auth/refresh
```

#### Wallet & Points
```
GET  /api/v1/wallet/balance
GET  /api/v1/wallet/ledger
POST /api/v1/wallet/redeem
```

#### Stations & Refills
```
GET  /api/v1/stations
GET  /api/v1/pricing/current?stationId=1&kind=PETROL&grade=RON_95
POST /api/v1/refills
```

#### Admin Analytics
```
GET  /api/v1/admin/analytics/overview
GET  /api/v1/admin/analytics/top-customers
```

## 📱 Mobile Experience

- **Mobile-first** responsive design
- **PWA** with offline capabilities
- **Touch-optimized** interfaces (44px+ targets)
- **Bottom navigation** for mobile
- **Skeleton loaders** for perceived performance
- **Optimistic UI** updates

## ⚙️ Configuration

### Environment Variables

```bash
# Database
DB_HOST=localhost
DB_NAME=binkat_oil
DB_USERNAME=binkat
DB_PASSWORD=binkat123

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your-super-secret-key
JWT_ACCESS_EXPIRATION=900000    # 15 minutes
JWT_REFRESH_EXPIRATION=604800000  # 7 days

# Points System
REDEMPTION_RATE_UZS_PER_POINT=10
MIN_REDEEM_POINTS=100
MAX_DISCOUNT_PCT=50
POINTS_EXPIRY_DAYS=365
```

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm run test
npm run test:e2e
```

### Integration Tests
Full integration tests use **Testcontainers** for PostgreSQL and Redis.

## 📈 Analytics Features

### Customer Leaderboards
- **Top by Refills**: Number of transactions
- **Top by Volume**: Liters/kWh/KG consumed
- **Petrol Grade Filtering**: Separate RON 92/95 analytics
- **Date Range Filtering**: Flexible time periods
- **Export Options**: CSV/Excel with applied filters

### Admin Dashboard
- Revenue and transaction summaries
- Active customer counts
- Points issuance and redemption rates
- Station performance metrics
- Real-time data with 5-minute freshness

## 🚀 Deployment

### Production Checklist
- [ ] Set strong JWT secrets
- [ ] Configure Redis password
- [ ] Set up TLS/SSL certificates
- [ ] Configure CORS origins
- [ ] Set up monitoring (Prometheus + Grafana)
- [ ] Configure log aggregation
- [ ] Set up automated backups

### Kubernetes Deployment
```bash
kubectl apply -f infra/k8s/
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Run linting and tests
6. Submit a pull request

### Code Quality
- **Backend**: Spotless + Checkstyle
- **Frontend**: ESLint + Prettier
- **CI/CD**: GitHub Actions with quality gates

## 📄 License

This project is proprietary software for Binkat Oil.

## 📞 Support

For technical support or questions:
- Email: tech@binkat.uz
- Documentation: [API Docs](http://localhost:8080/swagger-ui.html)

---

**Binkat Oil** - Powering customer loyalty through innovation 🚗⛽