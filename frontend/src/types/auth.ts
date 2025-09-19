export interface User {
  id: string
  name: string
  email: string
  phone?: string
  locale: string
  role: 'CUSTOMER' | 'OPERATOR' | 'ADMIN'
  tier?: {
    id: string
    code: 'SILVER' | 'GOLD' | 'PLATINUM'
    name: string
    multiplier: number
  }
  active: boolean
  createdAt: string
  updatedAt: string
}

export interface LoginCredentials {
  email: string
  password: string
}

export interface RegisterData {
  name: string
  email: string
  password: string
  phone?: string
  locale?: string
}

export interface AuthResponse {
  user: User
  accessToken: string
  refreshToken: string
  expiresIn: number
}

export interface RefreshResponse {
  accessToken: string
  refreshToken?: string
  expiresIn: number
}