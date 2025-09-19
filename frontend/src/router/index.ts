import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// Lazy load views for better performance
const HomeView = () => import('@/views/HomeView.vue')
const LoginView = () => import('@/views/auth/LoginView.vue')
const RegisterView = () => import('@/views/auth/RegisterView.vue')
const WalletView = () => import('@/views/WalletView.vue')
const StationsView = () => import('@/views/StationsView.vue')
const HistoryView = () => import('@/views/HistoryView.vue')
const ProfileView = () => import('@/views/ProfileView.vue')
const AdminDashboardView = () => import('@/views/admin/DashboardView.vue')
const AdminAnalyticsView = () => import('@/views/admin/AnalyticsView.vue')
const OperatorPOSView = () => import('@/views/operator/POSView.vue')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { guest: true }
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { guest: true }
    },
    {
      path: '/wallet',
      name: 'wallet',
      component: WalletView,
      meta: { requiresAuth: true }
    },
    {
      path: '/stations',
      name: 'stations',
      component: StationsView,
      meta: { requiresAuth: true }
    },
    {
      path: '/history',
      name: 'history',
      component: HistoryView,
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin',
      name: 'admin-dashboard',
      component: AdminDashboardView,
      meta: { requiresAuth: true, role: 'ADMIN' }
    },
    {
      path: '/admin/analytics',
      name: 'admin-analytics',
      component: AdminAnalyticsView,
      meta: { requiresAuth: true, role: 'ADMIN' }
    },
    {
      path: '/operator/pos',
      name: 'operator-pos',
      component: OperatorPOSView,
      meta: { requiresAuth: true, role: 'OPERATOR' }
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ]
})

// Navigation guards
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  
  // Check if route requires authentication
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return next({ name: 'login' })
  }
  
  // Check if user is already authenticated and trying to access guest routes
  if (to.meta.guest && authStore.isAuthenticated) {
    return next({ name: 'home' })
  }
  
  // Check role-based access
  if (to.meta.role && authStore.user?.role !== to.meta.role) {
    return next({ name: 'home' })
  }
  
  next()
})

export default router