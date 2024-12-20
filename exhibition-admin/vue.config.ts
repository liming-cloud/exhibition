import { defineConfig } from '@vue/cli-service'
import DefineOptions from 'unplugin-vue-define-options/webpack'

export default defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      DefineOptions()
    ],
    resolve: {
      extensions: ['.ts', '.js', '.vue', '.json']
    }
  }
}) 