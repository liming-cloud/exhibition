import {type App as VueApp, createApp} from 'vue'
import {createPinia} from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import './assets/main.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import './styles/global.css'

const app: VueApp = createApp(App)

// 注册所有图标
Object.entries(ElementPlusIconsVue).forEach(([key, component]) => {
  app.component(key, component)
})

const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(ElementPlus, {
  size: 'default',
  zIndex: 3000,
  locale: zhCn,
})

app.mount('#app')
