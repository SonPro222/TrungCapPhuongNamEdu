import { chuongTrinhRoutes } from '@/modules/chuongTrinh/chuongTrinhRouter.js'
import { daoTaoRoutes } from '@/modules/daoTao/daoTaoRouter.js'
import { heThongRoutes } from '@/modules/heThong/heThongRouter.js'
import { sinhVienRoutes } from '@/modules/sinhVien/sinhVienRouter.js'
import { giangDayRoutes } from '@/modules/giangDay/giangDayRouter.js'
export const moduleRoutes = [
  ...chuongTrinhRoutes,
  ...daoTaoRoutes,
    ...heThongRoutes,
  ...sinhVienRoutes,
    ...giangDayRoutes
]
