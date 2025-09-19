import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createI18n } from 'vue-i18n'
import App from './App.vue'
import router from './router'

// Import global styles
import './assets/main.css'

// Import locales
import en from './locales/en.json'
import ru from './locales/ru.json'
import uzLatn from './locales/uz-Latn.json'

// Create i18n instance
const i18n = createI18n({
  legacy: false,
  locale: 'uz-Latn',
  fallbackLocale: 'en',
  messages: {
    en,
    ru,
    'uz-Latn': uzLatn
  }
})

// Create app
const app = createApp(App)

// Use plugins
app.use(createPinia())
app.use(router)
app.use(i18n)

// Mount app
app.mount('#app')