import { thongTinTruong } from './thongTinTruongData.js'
import { menuPublic } from './menuPublicData.js'

import {
    danhSachNganhDaoTao,
    danhMucNganhDaoTao
} from './nganhDaoTaoData.js'

import {
    danhMucTinTuc,
    tinTucNoiBat,
    danhSachTinTuc
} from './tinTucData.js'

import {
    thongKeTuyenSinh,
    quyTrinhTuyenSinh,
    hoSoTuyenSinh
} from './tuyenSinhData.js'

import {
    lichSuPhatTrien,
    doiNguGiangVien
} from './gioiThieuData.js'

import {
    thongTinLienHe,
    phongBanHoTro
} from './lienHeData.js'

import { danhSachCoSoVatChat } from './coSoVatChatData.js'

export {
    thongTinTruong,
    menuPublic,
    danhSachNganhDaoTao,
    danhMucNganhDaoTao,
    danhMucTinTuc,
    tinTucNoiBat,
    danhSachTinTuc,
    thongKeTuyenSinh,
    quyTrinhTuyenSinh,
    hoSoTuyenSinh,
    lichSuPhatTrien,
    doiNguGiangVien,
    thongTinLienHe,
    phongBanHoTro,
    danhSachCoSoVatChat
}

export const nganhDaoTaoNoiBat = danhSachNganhDaoTao

export const quyTrinhXetTuyen = [
    {
        num: '01',
        title: 'Đăng ký',
        desc: 'Điền thông tin đăng ký trực tuyến hoặc tại trường',
        icon: '📝'
    },
    {
        num: '02',
        title: 'Xét hồ sơ',
        desc: 'Nhà trường tiếp nhận và xét duyệt hồ sơ',
        icon: '📁'
    },
    {
        num: '03',
        title: 'Nhập học',
        desc: 'Nhận thông báo trúng tuyển và hoàn tất thủ tục',
        icon: '📄'
    },
    {
        num: '04',
        title: 'Bắt đầu học',
        desc: 'Tham gia học tập và trải nghiệm tại trường',
        icon: '🎓'
    }
]

export const thongKeTrangChu = [
    {
        icon: '🎓',
        number: '10.000+',
        label: 'Học viên đã và đang học'
    },
    {
        icon: '📚',
        number: '30+',
        label: 'Ngành đào tạo'
    },
    {
        icon: '👥',
        number: '200+',
        label: 'Doanh nghiệp đối tác'
    },
    {
        icon: '💙',
        number: '95%',
        label: 'Sinh viên có việc làm sau tốt nghiệp'
    },
    {
        icon: '🏆',
        number: '15+',
        label: 'Năm xây dựng và phát triển'
    }
]

export const tinTuc = danhSachTinTuc

export const camNhanHocVien = [
    {
        name: 'Nguyễn Thị Mai',
        role: 'Khoa Điều dưỡng',
        avatar: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=200&q=80',
        quote: 'Môi trường học tập thân thiện, giảng viên nhiệt tình. Em tự tin hơn rất nhiều sau khi được thực hành tại bệnh viện.'
    },
    {
        name: 'Trần Minh Quân',
        role: 'Khoa CNTT',
        avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=200&q=80',
        quote: 'Được học thực hành nhiều, tham gia dự án thực tế giúp em có việc làm ngay sau khi tốt nghiệp.'
    },
    {
        name: 'Lê Thanh Hằng',
        role: 'Khoa Du lịch',
        avatar: 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?auto=format&fit=crop&w=200&q=80',
        quote: 'Các thầy cô tận tâm, chương trình học sát thực tế, giúp em phát triển kỹ năng và tự tin trong công việc.'
    }
]

export const coSoVatChat = danhSachCoSoVatChat