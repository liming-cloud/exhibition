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
import { onBeforeUnmount, ref, shallowRef, watch, nextTick } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import { ElMessage } from 'element-plus'
import { getImageUrl } from '@/utils/request'

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
      server: '/api/file/upload',
      fieldName: 'file',
      maxFileSize: 5 * 1024 * 1024, // 5MB
      maxNumberOfFiles: 10,
      allowedFileTypes: ['image/*'],
      customInsert(res: any, insertFn: Function) {
        if (res.code === 200) {
          insertFn(getImageUrl(res.data), '', '')
        } else {
          ElMessage.error('图片上传失败')
        }
      },
      onError(_file: File, err: any) {
        ElMessage.error('上传图片失败')
        console.error('上传图片失败:', err)
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