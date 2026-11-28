// 后台「联系我们」维护的联系信息，页头页脚、关于我们、联系我们共用同一个 key：
// defer：后调用的组件复用进行中的请求（默认 cancel 会重发）；getCachedData：客户端跳转复用已取到的数据
export const useContactInfo = () => {
  const api = useApi()
  return useAsyncData('site-contact', () => api.contact.getInfo(), {
    dedupe: 'defer',
    getCachedData: (key, nuxtApp) => nuxtApp.payload.data[key] ?? nuxtApp.static.data[key]
  })
}

export const useSiteContact = () => {
  const { data } = useContactInfo()
  return computed(() => ({
    hotline: data.value?.phone?.[0] ?? '',
    mobile: data.value?.phone?.[1] ?? '',
    email: data.value?.email ?? '',
    address: data.value?.address ?? '',
    profile: data.value?.companyProfile ?? ''
  }))
}
