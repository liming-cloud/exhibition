/**
 * 富文本转纯文本摘要，用于页面 description
 */
export const toSummary = (html?: string, length = 120) => {
  if (!html) return ''
  return html
    .replace(/<[^>]+>/g, '')
    .replace(/&nbsp;/g, ' ')
    .replace(/\s+/g, ' ')
    .trim()
    .slice(0, length)
}
