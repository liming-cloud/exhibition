import {ElMessage} from 'element-plus'

/** 图片最长边上限，超过的在上传前等比缩小 */
const MAX_SIDE = 1920

/**
 * 上传前压缩：最长边超过 1920px 的图片用 Canvas 等比缩小，其他原样返回。
 * GIF（可能是动图）和 SVG 不处理。
 */
export async function compressImage(file: File): Promise<File> {
  if (!file.type.startsWith('image/') || file.type === 'image/gif' || file.type === 'image/svg+xml') {
    return file
  }
  const bitmap = await createImageBitmap(file).catch(() => null)
  if (!bitmap) return file
  const scale = MAX_SIDE / Math.max(bitmap.width, bitmap.height)
  if (scale >= 1) {
    bitmap.close()
    return file
  }
  const canvas = document.createElement('canvas')
  canvas.width = Math.round(bitmap.width * scale)
  canvas.height = Math.round(bitmap.height * scale)
  canvas.getContext('2d')!.drawImage(bitmap, 0, 0, canvas.width, canvas.height)
  bitmap.close()
  // PNG 保留透明通道，其余转 JPEG
  const type = file.type === 'image/png' ? 'image/png' : 'image/jpeg'
  const blob = await new Promise<Blob | null>((resolve) => canvas.toBlob(resolve, type, 0.85))
  if (!blob) return file
  const name = type === 'image/jpeg' ? file.name.replace(/\.\w+$/, '') + '.jpg' : file.name
  return new File([blob], name, {type})
}

/**
 * 封面、轮播图上传前的处理：校验类型 → 压缩 → 校验大小。返回 false 时取消上传。
 */
export async function prepareImage(file: File, maxMB: number): Promise<File | false> {
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  const result = await compressImage(file)
  if (result.size / 1024 / 1024 >= maxMB) {
    ElMessage.error(`图片压缩后仍超过 ${maxMB}MB，请换一张图片`)
    return false
  }
  return result
}
