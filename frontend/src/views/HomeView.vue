<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center py-6">
          <div class="flex items-center">
            <h1 class="text-2xl font-bold text-binkat-blue">
              {{ $t('app.name') }}
            </h1>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-gray-700">{{ user?.name }}</span>
            <button 
              @click="handleLogout"
              class="btn-secondary"
            >
              {{ $t('auth.logout') }}
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Wallet Card -->
      <div class="card mb-8">
        <div class="card-body">
          <div class="text-center">
            <h2 class="text-lg font-medium text-gray-900 mb-2">
              {{ $t('wallet.balance') }}
            </h2>
            <div class="text-4xl font-bold text-binkat-blue mb-2">
              {{ walletStore.formatPoints(walletStore.balance) }}
            </div>
            <div class="text-gray-600 mb-4">
              {{ walletStore.formatUzs(walletStore.estimatedValueUzs) }} {{ $t('common.uzs') }}
            </div>
            
            <!-- Quick Stats -->
            <div class="grid grid-cols-2 gap-4 mt-6">
              <div class="text-center">
                <div class="text-sm text-gray-500">{{ $t('wallet.earnedToday') }}</div>
                <div class="text-lg font-semibold text-green-600">
                  +{{ walletStore.formatPoints(walletStore.earnedToday) }}
                </div>
              </div>
              <div class="text-center">
                <div class="text-sm text-gray-500">{{ $t('wallet.earned30Days') }}</div>
                <div class="text-lg font-semibold text-green-600">
                  +{{ walletStore.formatPoints(walletStore.earned30Days) }}
                </div>
              </div>
            </div>
            
            <!-- Actions -->
            <div class="mt-6">
              <router-link 
                to="/stations" 
                class="btn-primary mr-2"
              >
                {{ $t('wallet.scanAtPump') }}
              </router-link>
              <router-link 
                to="/wallet" 
                class="btn-secondary"
              >
                {{ $t('wallet.redeem') }}
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <router-link 
          to="/stations" 
          class="card card-body text-center hover:shadow-lg transition-shadow"
        >
          <div class="text-binkat-blue text-2xl mb-2">⛽</div>
          <h3 class="font-medium text-gray-900">{{ $t('nav.stations') }}</h3>
        </router-link>
        
        <router-link 
          to="/history" 
          class="card card-body text-center hover:shadow-lg transition-shadow"
        >
          <div class="text-binkat-blue text-2xl mb-2">📊</div>
          <h3 class="font-medium text-gray-900">{{ $t('nav.history') }}</h3>
        </router-link>
        
        <router-link 
          to="/wallet" 
          class="card card-body text-center hover:shadow-lg transition-shadow"
        >
          <div class="text-binkat-blue text-2xl mb-2">💰</div>
          <h3 class="font-medium text-gray-900">{{ $t('nav.wallet') }}</h3>
        </router-link>
        
        <router-link 
          to="/profile" 
          class="card card-body text-center hover:shadow-lg transition-shadow"
        >
          <div class="text-binkat-blue text-2xl mb-2">👤</div>
          <h3 class="font-medium text-gray-900">{{ $t('nav.profile') }}</h3>
        </router-link>
      </div>

      <!-- Loading State -->
      <div v-if="walletStore.loading" class="text-center py-8">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-binkat-blue mx-auto"></div>
        <p class="mt-2 text-gray-600">{{ $t('common.loading') }}</p>
      </div>

      <!-- Error State -->
      <div v-if="walletStore.error" class="card mb-8">
        <div class="card-body text-center">
          <div class="text-red-600 text-lg">{{ $t('common.error') }}</div>
          <p class="text-gray-600 mt-2">{{ walletStore.error }}</p>
        </div>
      </div>
    </main>

    <!-- Bottom Navigation (Mobile) -->
    <nav class="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 lg:hidden">
      <div class="grid grid-cols-5">
        <router-link 
          to="/" 
          class="flex flex-col items-center py-2 px-1 text-binkat-blue"
        >
          <span class="text-xl">🏠</span>
          <span class="text-xs mt-1">{{ $t('nav.home') }}</span>
        </router-link>
        
        <router-link 
          to="/stations" 
          class="flex flex-col items-center py-2 px-1 text-gray-600"
        >
          <span class="text-xl">⛽</span>
          <span class="text-xs mt-1">{{ $t('nav.stations') }}</span>
        </router-link>
        
        <router-link 
          to="/wallet" 
          class="flex flex-col items-center py-2 px-1 text-gray-600"
        >
          <span class="text-xl">💰</span>
          <span class="text-xs mt-1">{{ $t('nav.wallet') }}</span>
        </router-link>
        
        <router-link 
          to="/history" 
          class="flex flex-col items-center py-2 px-1 text-gray-600"
        >
          <span class="text-xl">📊</span>
          <span class="text-xs mt-1">{{ $t('nav.history') }}</span>
        </router-link>
        
        <router-link 
          to="/profile" 
          class="flex flex-col items-center py-2 px-1 text-gray-600"
        >
          <span class="text-xl">👤</span>
          <span class="text-xs mt-1">{{ $t('nav.profile') }}</span>
        </router-link>
      </div>
    </nav>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useWalletStore } from '@/stores/wallet'

const router = useRouter()
const authStore = useAuthStore()
const walletStore = useWalletStore()

const user = computed(() => authStore.user)

async function handleLogout() {
  try {
    await authStore.logout()
    router.push('/login')
  } catch (error) {
    console.error('Logout failed:', error)
  }
}

onMounted(async () => {
  try {
    await walletStore.fetchWallet()
    await walletStore.fetchLedger(20) // Get recent transactions
  } catch (error) {
    console.error('Failed to load home data:', error)
  }
})
</script>