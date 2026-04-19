# Deployment Guide - Railway/Render (Single Instance)

## Architecture
All 5 backend services run in a single Railway container:
- **user-service** (port 8081)
- **group-service** (port 8082)
- **expence-service** (port 8083)
- **user-group-service** (port 8084)
- **api-service** (port 8085) - API Gateway (public entry point)

Services communicate via `localhost` URLs - no service discovery needed.

---

## Prerequisites
- Docker installed locally (for testing)
- Railway/Render account
- Supabase database (already configured)

---

## Step 1: Set Environment Variables

In Railway/Render dashboard, add these environment variables:

| Variable | Value |
|----------|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?sslmode=require&prepareThreshold=0` |
| `SPRING_DATASOURCE_USERNAME` | `postgres.naviyfnqtkimwjohuabz` |
| `SPRING_DATASOURCE_PASSWORD` | Your Supabase password |
| `EUREKA_ZIPKIN_ENABLED` | `false` |

Optional (defaults work for single-instance):
| `USER_SERVICE_URL` | `http://localhost:8081` |
| `GROUP_SERVICE_URL` | `http://localhost:8082` |
| `EXPENCE_SERVICE_URL` | `http://localhost:8083` |
| `USER_GROUP_SERVICE_URL` | `http://localhost:8084` |

---

## Step 2: Deploy to Railway

### Option A: Deploy via GitHub (Recommended)
1. Push your code to GitHub
2. Go to [railway.app](https://railway.app)
3. Click "New Project" → "Deploy from GitHub repo"
4. Select your repository
5. Add environment variables from Step 1
6. Click "Deploy"

### Option B: Deploy via Docker
```bash
# Build the image
docker build -t expense-distributer .

# Test locally
docker run -p 8085:8085 \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  -e EUREKA_ZIPKIN_ENABLED=false \
  expense-distributer

# Push to Railway
railway up
```

---

## Step 3: Update Frontend CORS

Your frontend (Vercel) needs to point to the new API URL:

1. After deployment, Railway gives you a URL like: `https://your-app.railway.app`
2. Update your frontend's API base URL to: `https://your-app.railway.app/api-service`

---

## Step 4: Verify Deployment

1. Check Railway logs for startup messages
2. Test endpoints:
   - `GET https://your-app.railway.app/user/...`
   - `GET https://your-app.railway.app/group/...`
   - `GET https://your-app.railway.app/expence/...`
3. Check health: `GET https://your-app.railway.app/actuator/health`

---

## Troubleshooting

### Services not starting
- Check RAM usage: 5 Java services need ~512MB-1GB
- Railway free tier: 512MB may be tight, consider upgrading

### Connection refused errors
- Wait 30-60 seconds for all services to start
- Check logs for startup order issues

### Database connection failed
- Verify Supabase credentials
- Ensure `sslmode=require` in connection URL

---

## Memory Optimization

If you hit memory limits, adjust JVM heap in `start.sh`:
```bash
java -Xms64m -Xmx128m -jar user-service.jar &
```

Lower values = less memory, slower startup.
