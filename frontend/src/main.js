
// Bootstrap
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
//css
import './styles/main.css'
// Vue
import { createApp } from 'vue'
import App from './App.vue'

// Router
import router from '@/routerCha/router.js'

const app = createApp(App)

app.use(router)

app.mount('#app')