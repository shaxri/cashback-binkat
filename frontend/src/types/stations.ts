export interface Station {
  id: string
  name: string
  type: 'GAS' | 'PROPANE' | 'EV'
  address: string
  latitude?: number
  longitude?: number
  active: boolean
  pumps: PumpOrCharger[]
  prices: PricePlan[]
  distance?: number // calculated distance from user
}

export interface PumpOrCharger {
  id: string
  stationId: string
  kind: 'PETROL' | 'DIESEL' | 'PROPANE' | 'EV_AC' | 'EV_DC'
  grade?: 'RON_92' | 'RON_95' // Only for PETROL
  code: string
  active: boolean
}

export interface PricePlan {
  id: string
  stationId: string
  kind: 'PETROL' | 'DIESEL' | 'PROPANE' | 'EV_AC' | 'EV_DC'
  grade?: 'RON_92' | 'RON_95' // Only for PETROL
  unit: 'LITER' | 'KG' | 'KWH'
  basePriceUzs: number
  effectiveFrom: string
  effectiveTo?: string
}

export interface RefillTransaction {
  id: string
  userId: string
  stationId: string
  pumpId: string
  kind: 'PETROL' | 'DIESEL' | 'PROPANE' | 'EV_AC' | 'EV_DC'
  grade?: 'RON_92' | 'RON_95'
  unit: 'LITER' | 'KG' | 'KWH'
  quantity: number
  unitPriceUzs: number
  totalUzs: number
  paidUzs: number
  pointsEarned: number
  pointsRedeemed?: number
  discountUzs?: number
  status: 'PENDING' | 'COMPLETED' | 'CANCELLED' | 'FAILED'
  occurredAt: string
  station: {
    id: string
    name: string
  }
}

export interface CreateRefillRequest {
  stationId: string
  pumpId: string
  kind: 'PETROL' | 'DIESEL' | 'PROPANE' | 'EV_AC' | 'EV_DC'
  grade?: 'RON_92' | 'RON_95'
  unit: 'LITER' | 'KG' | 'KWH'
  quantity: number
  unitPriceUzs: number
  redeemPoints?: number
}