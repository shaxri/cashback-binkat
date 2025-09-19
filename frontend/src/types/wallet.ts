export interface Wallet {
  id: string
  userId: string
  balancePoints: number
  estimatedValueUzs: number
  updatedAt: string
}

export interface PointsLedgerEntry {
  id: string
  userId: string
  sourceTransactionId?: string
  deltaPoints: number
  reason: 'EARN' | 'REDEEM' | 'ADJUST' | 'EXPIRE'
  createdAt: string
  expiresAt: string | null
}

export interface LedgerResponse {
  entries: PointsLedgerEntry[]
  nextCursor?: string
  hasMore: boolean
}

export interface RedemptionPreview {
  maxPoints: number
  suggestedPoints: number
  discountUzs: number
  redemptionRate: number
}

export interface RedemptionRequest {
  transactionId: string
  points: number
}

export interface RedemptionResult {
  success: boolean
  discountUzs: number
  pointsRedeemed: number
  transactionId: string
}