export const HE_THONG_VIEW_CONFIG = {
    sinhVien: {
        title: 'Tài khoản sinh viên',
        description: 'Quản lý tài khoản sinh viên, vai trò, quyền và liên kết tới hồ sơ sinh viên.',
        tableTitle: 'Danh sách tài khoản sinh viên',
        accountTypes: ['sinh_vien'],
        statuses: [
            { value: 'dang_hoc', label: 'Đang học' },
            { value: 'bao_luu', label: 'Bảo lưu' },
            { value: 'tot_nghiep', label: 'Tốt nghiệp' },
            { value: 'nghi_hoc', label: 'Nghỉ học' },
        ],
    },
    giangVien: {
        title: 'Tài khoản giảng viên',
        description: 'Quản lý tài khoản giảng viên, vai trò, quyền và trạng thái tài khoản.',
        tableTitle: 'Danh sách tài khoản giảng viên',
        accountTypes: ['giao_vien', 'giang_vien'],
        statuses: [
            { value: 'cho_kich_hoat', label: 'Chờ kích hoạt' },
            { value: 'da_kich_hoat', label: 'Đã kích hoạt' },
            { value: 'bi_khoa', label: 'Bị khóa' },
        ],
    },
    nhanVien: {
        title: 'Nhân viên',
        description: 'Quản lý hồ sơ nhân viên và tài khoản phân quyền.',
        tableTitle: 'Danh sách nhân viên',
        accountTypes: ['nhan_vien'],
        statuses: [
            { value: 'dang_lam', label: 'Đang làm' },
            { value: 'tam_nghi', label: 'Tạm nghỉ' },
            { value: 'nghi_viec', label: 'Nghỉ việc' },
        ],
    },
    daoTaoAdmin: {
        title: 'Đào tạo và admin',
        description: 'Quản lý nhân sự phòng đào tạo, tài khoản admin và phân quyền hệ thống.',
        tableTitle: 'Danh sách đào tạo / admin',
        accountTypes: ['dao_tao', 'admin'],
        statuses: [
            { value: 'dang_lam', label: 'Đang làm' },
            { value: 'tam_nghi', label: 'Tạm nghỉ' },
            { value: 'nghi_viec', label: 'Nghỉ việc' },
        ],
    },
    vaiTroQuyen: {
        title: 'Vai trò / Quyền',
        description: 'Quản lý danh mục vai trò và quyền trong hệ thống.',
        tableTitle: 'Danh sách vai trò và quyền',
        accountTypes: [],
        statuses: [],
    },
    lienKet: {
        title: 'Liên kết phân quyền',
        description: 'Gán vai trò cho tài khoản và gán quyền cho vai trò.',
        tableTitle: 'Danh sách liên kết phân quyền',
        accountTypes: [],
        statuses: [],
    },
};
