// server/api/carousel.js
export default defineEventHandler(() => {
    return [
        {
            image: '/images/carousel/slide1.jpg',
            title: '2024国际科技创新展',
            description: '汇聚全球顶尖科技企业，展示最新技术创新成果'
        },
        {
            image: '/images/carousel/slide2.jpg',
            title: '世界工业装备博览会',
            description: '展示全球领先的工业自动化解决方案'
        },
        {
            image: '/images/carousel/slide3.jpg',
            title: '国际汽车工业展览会',
            description: '未来出行方案的完美展示平台'
        }
    ]
})