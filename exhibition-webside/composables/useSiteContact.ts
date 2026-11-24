// 页头页脚共用的联系信息（后台「联系我们」维护），同一个 key 只请求一次
export const useSiteContact = () => {
  const api = useApi()
  const { data } = useAsyncData('site-contact', () => api.contact.getInfo())
  return computed(() => ({
    hotline: data.value?.phone?.[0] ?? '',
    mobile: data.value?.phone?.[1] ?? '',
    email: data.value?.email ?? '',
    address: data.value?.address ?? '',
    profile: data.value?.companyProfile ?? ''
  }))
}
