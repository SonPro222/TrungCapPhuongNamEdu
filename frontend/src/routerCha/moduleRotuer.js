import { chuongTrinhRoutes } from '@/modules/chuongTrinh/chuongTrinhRouter.js'
import { daoTaoRoutes } from '@/modules/daoTao/daoTaoRouter.js'

export const moduleRoutes = [
  ...chuongTrinhRoutes,
  ...daoTaoRoutes
]
