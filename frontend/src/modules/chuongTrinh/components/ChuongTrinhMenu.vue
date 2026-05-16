<template>
    <nav class="chuong-trinh-menu">
        <RouterLink to="/admin/chuong-trinh">Chương trình</RouterLink>
        <RouterLink to="/admin/chuong-trinh/version">Phiên bản</RouterLink>
        <RouterLink to="/admin/chuong-trinh/mon">Môn trong CT</RouterLink>
        <RouterLink to="/admin/chuong-trinh/nhom-kien-thuc">Nhóm kiến thức</RouterLink>
        <RouterLink to="/admin/chuong-trinh/muc-tieu">Mục tiêu</RouterLink>
        <RouterLink to="/admin/chuong-trinh/nang-luc-dau-ra">Năng lực đầu ra</RouterLink>
        <RouterLink to="/admin/chuong-trinh/vi-tri-viec-lam">Vị trí việc làm</RouterLink>
        <RouterLink to="/admin/chuong-trinh/dieu-kien-tot-nghiep">Điều kiện TN</RouterLink>
        <RouterLink to="/admin/chuong-trinh/nhom-tu-chon">Nhóm tự chọn</RouterLink>
        <RouterLink to="/admin/chuong-trinh/mon-tu-chon">Môn tự chọn</RouterLink>
        <RouterLink to="/admin/chuong-trinh/mon-tien-quyet">Môn tiên quyết</RouterLink>
        <RouterLink to="/admin/chuong-trinh/mon-hoc">Môn học</RouterLink>
        <RouterLink to="/admin/chuong-trinh/dieu-kien-mon-hoc">Điều kiện môn học</RouterLink>
        <RouterLink to="/admin/chuong-trinh/quy-doi-diem">Quy đổi điểm</RouterLink>
        <RouterLink to="/admin/chuong-trinh/syllabus-chuong-trinh">Syllabus chương trình</RouterLink>
        <RouterLink to="/admin/chuong-trinh/syllabus-mon-hoc">Syllabus môn học</RouterLink>
        <RouterLink to="/admin/chuong-trinh/syllabus-chuong-bai">Syllabus chương bài</RouterLink>
        <RouterLink to="/admin/chuong-trinh/syllabus-tai-lieu">Syllabus tài liệu</RouterLink>
        <RouterLink to="/admin/chuong-trinh/xay-dung">
        Xây dựng chương trình
        </RouterLink>
    </nav>
</template>

<style scoped>
    .chuong-trinh-menu {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
}

    .chuong-trinh-menu a {
    padding: 8px 12px;
    border: 1px solid var(--color-border);
    border-radius: var(--radius);
    background: var(--color-white);
    color: var(--color-text);
}

    .chuong-trinh-menu a.router-link-active {
    background: var(--color-primary);
    color: white;
    border-color: var(--color-primary);
}
</style>