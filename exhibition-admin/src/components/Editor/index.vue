<template>
  <div class="editor-container">
    <Toolbar
      class="editor-toolbar"
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />
    <Editor
      class="editor-main"
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />
  </div>
</template>

<script lang="ts" setup>
import {nextTick, onBeforeUnmount, ref, shallowRef, watch} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import {ElMessage} from 'element-plus'
import {getImageUrl} from '@/utils/request'
import {prepareImage} from '@/utils/image'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('')

// 模式
const mode = ref('default')

// 工具栏配置
const toolbarConfig = {
  excludeKeys: []
}

// 编辑器配置
const editorConfig = {
  placeholder: '请输入内容...',
  autoFocus: false,
  MENU_CONF: {
    uploadImage: {
      allowedFileTypes: ['image/*'],
      // 先压缩再上传（onBeforeUpload 不支持异步，所以自己上传）
      async customUpload(file: File, insertFn: Function) {
        const image = await prepareImage(file, 5)
        if (!image) return
        try {
          const body = new FormData()
          body.append('file', image)
          const res = await (await fetch('/api/file/upload', {method: 'POST', body})).json()
          if (res.code !== 200) throw new Error(res.msg)
          insertFn(getImageUrl(res.data), '', '')
        } catch (err) {
          ElMessage.error('上传图片失败')
          console.error('上传图片失败:', err)
        }
      }
    }
  }
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

// 初始化编辑器
const handleCreated = (editor: any) => {
  editorRef.value = editor
  nextTick(() => {
    valueHtml.value = props.modelValue
  })
}

// 编辑器内容变化时触发
const handleChange = (editor: any) => {
  emit('update:modelValue', editor.getHtml())
}

// 监听父组件传入的内容变化
watch(() => props.modelValue, (newValue) => {
  if (newValue !== valueHtml.value) {
    valueHtml.value = newValue
  }
})
</script>

<style scoped>
.editor-container {
  z-index: 1;
  border: 1px solid #ccc;
  margin: 20px auto;
}

.editor-toolbar {
  border-bottom: 1px solid #ccc;
}

.editor-main {
  height: 500px;
  overflow-y: hidden;
}
</style> 