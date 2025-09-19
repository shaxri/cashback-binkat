import axios, { AxiosInstance, AxiosRequestConfig } from 'axios'
import type { 
  AuthResponse, 
  LoginCredentials, 
  RegisterData, 
  RefreshResponse,
  User 
} from '@/types/auth'
import type { 
  Wallet, 
  LedgerResponse, 
  RedemptionPreview, 
  RedemptionResult 
} from '@/types/wallet'
import type { 
  Station, 
  RefillTransaction, 
  CreateRefillRequest,
  PricePlan
} from '@/types/stations'

// Create axios instance
const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor to handle token refresh
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config
    
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true
      
      try {
        const refreshToken = localStorage.getItem('refreshToken')
        if (refreshToken) {
          const response = await authApi.refresh(refreshToken)
          localStorage.setItem('token', response.accessToken)
          
          // Retry original request with new token
          originalRequest.headers.Authorization = `Bearer ${response.accessToken}`
          return api(originalRequest)
        }
      } catch (refreshError) {
        // Refresh failed, redirect to login
        localStorage.removeItem('token')
        localStorage.removeItem('refreshToken')
        window.location.href = '/login'
      }
    }
    
    return Promise.reject(error)
  }
)

// Auth API
export const authApi = {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    const response = await api.post('/auth/login', credentials)
    return response.data
  },

  async register(data: RegisterData): Promise<AuthResponse> {
    const response = await api.post('/auth/register', data)
    return response.data
  },

  async refresh(refreshToken: string): Promise<RefreshResponse> {
    const response = await api.post('/auth/refresh', { refreshToken })
    return response.data
  },

  async logout(refreshToken: string): Promise<void> {
    await api.post('/auth/logout', { refreshToken })
  },

  async getMe(): Promise<User> {
    const response = await api.get('/users/me')
    return response.data
  },
}

// Wallet API
export const walletApi = {
  async getBalance(): Promise<Wallet> {
    const response = await api.get('/wallet/balance')
    return response.data
  },

  async getLedger(limit = 50, cursor?: string): Promise<LedgerResponse> {
    const params = new URLSearchParams({ limit: limit.toString() })
    if (cursor) params.append('cursor', cursor)
    
    const response = await api.get(`/wallet/ledger?${params}`)
    return response.data
  },

  async getRedemptionPreview(transactionId: string): Promise<RedemptionPreview> {
    const response = await api.post('/wallet/redeem-preview', { transactionId })
    return response.data
  },

  async redeemPoints(transactionId: string, points: number): Promise<RedemptionResult> {
    const response = await api.post('/wallet/redeem', { transactionId, points })
    return response.data
  },
}

// Stations API
export const stationsApi = {
  async getStations(active = true): Promise<Station[]> {
    const response = await api.get('/stations', { params: { active } })
    return response.data
  },

  async getStation(id: string): Promise<Station> {
    const response = await api.get(`/stations/${id}`)
    return response.data
  },

  async getCurrentPricing(stationId: string, kind: string, grade?: string): Promise<PricePlan> {
    const params = new URLSearchParams({ stationId, kind })
    if (grade) params.append('grade', grade)
    
    const response = await api.get(`/pricing/current?${params}`)
    return response.data
  },
}

// Refills API
export const refillsApi = {
  async createRefill(data: CreateRefillRequest): Promise<RefillTransaction> {
    const response = await api.post('/refills', data)
    return response.data
  },

  async getRefill(id: string): Promise<RefillTransaction> {
    const response = await api.get(`/refills/${id}`)
    return response.data
  },

  async getRefills(params?: {
    from?: string
    to?: string
    kind?: string
    grade?: string
    stationId?: string
    limit?: number
    cursor?: string
  }): Promise<{ refills: RefillTransaction[], nextCursor?: string, hasMore: boolean }> {
    const searchParams = new URLSearchParams()
    
    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined) {
          searchParams.append(key, value.toString())
        }
      })
    }
    
    const response = await api.get(`/refills?${searchParams}`)
    return response.data
  },
}

// Admin API (placeholder for future implementation)
export const adminApi = {
  // TODO: Implement admin analytics endpoints
}

export default api