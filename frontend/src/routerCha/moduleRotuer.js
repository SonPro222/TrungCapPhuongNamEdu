
import { daoTaoRoutes } from '@/modules/daoTao/daoTaoRouter.js'
import { heThongRoutes } from '@/modules/heThong/heThongRouter.js'
import { sinhVienRoutes } from '@/modules/sinhVien/sinhVienRouter.js'
import { giangDayRoutes } from '@/modules/giangDay/giangDayRouter.js'

export const moduleRoutes = [
    ...daoTaoRoutes,
    ...heThongRoutes,
    ...sinhVienRoutes,
    ...giangDayRoutes
]

