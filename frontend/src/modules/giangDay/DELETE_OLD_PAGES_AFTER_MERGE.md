# Các trang cũ có thể xóa sau khi gộp flow lớp học phần

Sau bản gộp này, router không còn import các trang chọn từng bước nữa. Có thể xóa các file sau trong `src/modules/giangDay/pages` hoặc thư mục module giảng dạy tương ứng:

```bash
rm src/modules/giangDay/pages/AdminGiangDayChonNganh.vue
rm src/modules/giangDay/pages/AdminGiangDayChonChuongTrinh.vue
rm src/modules/giangDay/pages/AdminGiangDayChonVersion.vue
rm src/modules/giangDay/pages/AdminGiangDayChonKy.vue
rm src/modules/giangDay/pages/AdminGiangDayLopHocPhanTheoKy.vue
```

File thay thế mới là:

```bash
src/modules/giangDay/pages/AdminGiangDayQuanLyLopHocPhan.vue
```

Các route cũ vẫn được giữ tên và redirect về màn hình tổng hợp mới để không vỡ link nội bộ.
