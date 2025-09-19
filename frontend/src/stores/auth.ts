import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginCredentials, RegisterData } from '@/types/auth'
import { authApi } from '@/utils/api'

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref<User | null>(null)
  const token = ref<string | null>(localStorage.getItem('token'))
  const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))
  const loading = ref(false)
  const error = ref<string | null>(null)

  // Getters
  const isAuthenticated = computed(() => !!token.value && !!user.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  const isOperator = computed(() => user.value?.role === 'OPERATOR')
  const isCustomer = computed(() => user.value?.role === 'CUSTOMER')

  // Actions
  async function login(credentials: LoginCredentials) {
    loading.value = true
    error.value = null

    try {
      const response = await authApi.login(credentials)
      
      token.value = response.accessToken
      refreshToken.value = response.refreshToken
      user.value = response.user

      // Store tokens in localStorage
      localStorage.setItem('token', response.accessToken)
      localStorage.setItem('refreshToken', response.refreshToken)

      return response
    } catch (err: any) {
      error.value = err.message || 'Login failed'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function register(data: RegisterData) {
    loading.value = true
    error.value = null

    try {
      const response = await authApi.register(data)
      
      token.value = response.accessToken
      refreshToken.value = response.refreshToken
      user.value = response.user

      // Store tokens in localStorage
      localStorage.setItem('token', response.accessToken)
      localStorage.setItem('refreshToken', response.refreshToken)

      return response
    } catch (err: any) {
      error.value = err.message || 'Registration failed'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function logout() {
    try {
      if (refreshToken.value) {
        await authApi.logout(refreshToken.value)
      }
    } catch (err) {
      console.warn('Logout API call failed:', err)
    } finally {
      // Clear state regardless of API call success
      user.value = null
      token.value = null
      refreshToken.value = null
      
      // Clear localStorage
      localStorage.removeItem('token')
      localStorage.removeItem('refreshToken')
    }
  }

  async function refreshAccessToken() {
    if (!refreshToken.value) {
      throw new Error('No refresh token available')
    }

    try {
      const response = await authApi.refresh(refreshToken.value)
      
      token.value = response.accessToken
      localStorage.setItem('token', response.accessToken)

      if (response.refreshToken) {
        refreshToken.value = response.refreshToken
        localStorage.setItem('refreshToken', response.refreshToken)
      }

      return response.accessToken
    } catch (err) {
      // If refresh fails, logout user
      await logout()
      throw err
    }
  }

  async function fetchUser() {
    if (!token.value) return

    try {
      const userData = await authApi.getMe()
      user.value = userData
    } catch (err) {
      console.error('Failed to fetch user:', err)
      // If user fetch fails, logout
      await logout()
    }
  }

  // Initialize auth state on store creation
  async function initialize() {
    if (token.value) {
      await fetchUser()
    }
  }

  return {
    // State
    user,
    token,
    refreshToken,
    loading,
    error,
    
    // Getters
    isAuthenticated,
    isAdmin,
    isOperator,
    isCustomer,
    
    // Actions
    login,
    register,
    logout,
    refreshAccessToken,
    fetchUser,
    initialize
  }
})