/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module '@wangeditor/editor-for-vue' {
  import { DefineComponent } from 'vue'
  export const Editor: DefineComponent<any, any, any>
  export const Toolbar: DefineComponent<any, any, any>
}

interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  readonly VITE_APP_API_URL: string
  readonly VITE_APP_BASE_API: string
  readonly VITE_APP_UPLOAD_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
} 