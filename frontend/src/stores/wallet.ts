import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Wallet, PointsLedgerEntry, RedemptionPreview } from '@/types/wallet'
import { walletApi } from '@/utils/api'

export const useWalletStore = defineStore('wallet', () => {
  // State
  const wallet = ref<Wallet | null>(null)
  const ledger = ref<PointsLedgerEntry[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  // Getters
  const balance = computed(() => wallet.value?.balancePoints || 0)
  const estimatedValueUzs = computed(() => {
    const redemptionRate = 10 // TODO: Get from config
    return balance.value * redemptionRate
  })

  const earnedToday = computed(() => {
    const today = new Date().toDateString()
    return ledger.value
      .filter(entry => 
        entry.reason === 'EARN' && 
        new Date(entry.createdAt).toDateString() === today
      )
      .reduce((sum, entry) => sum + entry.deltaPoints, 0)
  })

  const earned30Days = computed(() => {
    const thirtyDaysAgo = new Date()
    thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30)
    
    return ledger.value
      .filter(entry => 
        entry.reason === 'EARN' && 
        new Date(entry.createdAt) >= thirtyDaysAgo
      )
      .reduce((sum, entry) => sum + entry.deltaPoints, 0)
  })

  // Actions
  async function fetchWallet() {
    loading.value = true
    error.value = null

    try {
      const data = await walletApi.getBalance()
      wallet.value = data
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch wallet'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function fetchLedger(limit = 50, cursor?: string) {
    loading.value = true
    error.value = null

    try {
      const data = await walletApi.getLedger(limit, cursor)
      
      if (cursor) {
        // Append to existing ledger for pagination
        ledger.value = [...ledger.value, ...data.entries]
      } else {
        // Replace ledger for fresh fetch
        ledger.value = data.entries
      }

      return data
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch ledger'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function getRedemptionPreview(transactionId: string): Promise<RedemptionPreview> {
    try {
      return await walletApi.getRedemptionPreview(transactionId)
    } catch (err: any) {
      error.value = err.message || 'Failed to get redemption preview'
      throw err
    }
  }

  async function redeemPoints(transactionId: string, points: number) {
    loading.value = true
    error.value = null

    try {
      const result = await walletApi.redeemPoints(transactionId, points)
      
      // Refresh wallet balance after redemption
      await fetchWallet()
      
      // Add redemption to ledger
      ledger.value.unshift({
        id: `redemption-${Date.now()}`,
        userId: wallet.value?.userId || '',
        sourceTransactionId: transactionId,
        deltaPoints: -points,
        reason: 'REDEEM',
        createdAt: new Date().toISOString(),
        expiresAt: null
      })

      return result
    } catch (err: any) {
      error.value = err.message || 'Failed to redeem points'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Utility functions
  function formatPoints(points: number): string {
    return new Intl.NumberFormat('uz-Latn').format(points)
  }

  function formatUzs(amount: number): string {
    return new Intl.NumberFormat('uz-Latn', {
      style: 'currency',
      currency: 'UZS',
      minimumFractionDigits: 0
    }).format(amount)
  }

  return {
    // State
    wallet,
    ledger,
    loading,
    error,

    // Getters
    balance,
    estimatedValueUzs,
    earnedToday,
    earned30Days,

    // Actions
    fetchWallet,
    fetchLedger,
    getRedemptionPreview,
    redeemPoints,

    // Utils
    formatPoints,
    formatUzs
  }
})