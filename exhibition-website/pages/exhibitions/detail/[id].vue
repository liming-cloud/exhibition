<template>
  <div class="max-w-3xl mx-auto mt-12 px-4">
    <div class="flex items-center text-gray-600 mb-2">
      <i class="fas fa-map-marker-alt"></i>
      <span class="ml-1">{{ exhibition.location }}</span>
    </div>
    <div class="flex items-center text-gray-600 mb-4">
      <i class="fas fa-calendar"></i>
      <span class="ml-1">{{ exhibition.date }}</span>
    </div>
    <div v-if="exhibition.file">
      <div v-if="isPDF" class="pdf-container">
        <canvas ref="pdfCanvas"></canvas>
      </div>
      <div v-else class="docx-content" v-html="docxContent"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from '#app'
import exhibitionsData from '~/content/exhibitions.json'
import * as pdfjsLib from 'pdfjs-dist'

pdfjsLib.GlobalWorkerOptions.workerSrc =
    'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.10.377/pdf.worker.min.js'

const route = useRoute()
const exhibitionId = route.params.id
const exhibition = exhibitionsData.find(e => e.id === exhibitionId) || {}
const isPDF = ref(false)
const docxContent = ref('')
const pdfCanvas = ref(null)

onMounted(async () => {
  if (!exhibition.id) {
    console.error('展会数据未找到，ID参数错误')
    return
  }
  if (exhibition.file) {
    const fileExtension = exhibition.file.split('.').pop().toLowerCase()
    if (fileExtension === 'pdf') {
      isPDF.value = true
      await renderPDF()
    } else if (fileExtension === 'docx') {
      isPDF.value = false
      await loadPreConvertedHtml()
    }
  }
})

async function renderPDF() {
  const pdfUrl = exhibition.file
  const loadingTask = pdfjsLib.getDocument(pdfUrl)
  const pdf = await loadingTask.promise
  const page = await pdf.getPage(1)
  const viewport = page.getViewport({ scale: 1.5 })
  const context = pdfCanvas.value.getContext('2d')
  pdfCanvas.value.height = viewport.height
  pdfCanvas.value.width = viewport.width
  await page.render({ canvasContext: context, viewport }).promise
}

async function loadPreConvertedHtml() {
  try {
    // exhibition.file 如 "/exhibitions/africa/example.docx"
    // 转换后对应 "/docs/africa/example.html"
    const htmlUrl = '/docs/' + exhibition.file.replace(/^\/exhibitions\//, '').replace(/\.[^.]+$/, '.html')
    const response = await fetch(htmlUrl)
    if (!response.ok) {
      throw new Error('Failed to load converted HTML')
    }
    docxContent.value = await response.text()
  } catch (error) {
    console.error('Error loading converted HTML:', error)
  }
}
</script>

<style>
.pdf-container {
  border: 1px solid #ccc;
}
</style>