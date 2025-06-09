import { useCarouselApi } from './api/carousel'
import { useExhibitionApi } from './api/exhibition'
import { useInformationApi } from './api/information'
import { useSubsidiesApi } from './api/subsidies'
import { useCasesApi } from './api/cases'
import { useContactApi } from './api/contact'

export const useApi = () => {
  return {
    carousel: useCarouselApi(),
    exhibition: useExhibitionApi(),
    information: useInformationApi(),
    subsidies: useSubsidiesApi(),
    cases: useCasesApi(),
    contact: useContactApi()
  }
}

export type ApiInstance = ReturnType<typeof useApi>