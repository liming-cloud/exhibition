// 动效开关（只在浏览器端调用）：系统开了「减少动态效果」时不播放；
// 自动化浏览器（截图脚本）也不播放，否则整页截图里视口外的内容停在隐藏状态
export const motionAllowed = () =>
  !window.matchMedia('(prefers-reduced-motion: reduce)').matches && !navigator.webdriver

// 元素在首屏视口之外：只对这类元素做入场动画，首屏内容保持服务端渲染的样子，不会先消失再出现
export const belowFold = (el: Element) => el.getBoundingClientRect().top > window.innerHeight
