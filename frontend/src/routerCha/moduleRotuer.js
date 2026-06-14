import { daoTaoRoutes } from '@/modules/daoTao/daoTaoRouter.js'
import { heThongRoutes } from '@/modules/heThong/heThongRouter.js'
import { sinhVienRoutes } from '@/modules/sinhVien/sinhVienRouter.js'
import { giangDayRoutes } from '@/modules/giangDay/giangDayRouter.js'
import { xemChuongTrinhRoutes } from '@/modules/xemChuongTrinh/routes/xemChuongTrinhRouter.js'
import { lmsRoutes } from '@/modules/lms/lmsRouter.js'
import { tongTheRoutes } from '@/modules/daoTao/tongTheRouter.js'
import { trienKhaiRoutes } from '@/modules/trienkhai/trienKhaiRouter'
export const moduleRoutes = [
    ...daoTaoRoutes,
    ...heThongRoutes,
    ...sinhVienRoutes,
    ...giangDayRoutes,
    ...xemChuongTrinhRoutes,
    ...lmsRoutes,
        ...tongTheRoutes,
    ...trienKhaiRoutes
]