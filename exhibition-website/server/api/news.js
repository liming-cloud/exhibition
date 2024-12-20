// server/api/news.js
import newsData from '~/content/news.json'

export default defineEventHandler(() => {
    return newsData
})