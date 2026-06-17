<template>
  <section class="admin-public-page">
    <div class="admin-public-header">
      <div>
        <p class="eyebrow">Website public</p>

        <h1>Quản lý nội dung website</h1>

        <p>
          Quản lý các nội dung đang hiển thị ở trang public như banner trang chủ,
          thông tin trường, mạng xã hội, ngành đào tạo, tin tức, tuyển sinh,
          cảm nhận học viên và liên hệ tư vấn.
        </p>
      </div>

      <RouterLink :to="{ name: 'Public.TrangChu' }" target="_blank" class="preview-button">
        Xem website
        <i class="fa-solid fa-arrow-up-right-from-square"></i>
      </RouterLink>
    </div>

    <nav class="admin-public-menu">
      <RouterLink
          v-for="item in danhSachChucNang"
          :key="item.title"
          :to="item.to"
          class="admin-public-menu-item"
      >
        <i :class="item.icon"></i>
        <span>{{ item.title }}</span>
      </RouterLink>
    </nav>

    <div class="admin-public-content">
      <div class="content-left">
        <h2>Chọn một mục trên thanh menu để quản lý</h2>

        <p>
          Hiện tại dữ liệu module public đang lưu tạm bằng localStorage.
          Khi có backend API, phần giao diện admin này vẫn giữ lại được,
          chỉ thay phần store lưu localStorage sang gọi API thật.
        </p>

        <div class="quick-guide">
          <div>
            <i class="fa-solid fa-image"></i>
            <span>Banner trang chủ</span>
            <strong>Sửa hero đầu trang</strong>
          </div>

          <div>
            <i class="fa-solid fa-graduation-cap"></i>
            <span>Ngành đào tạo</span>
            <strong>Thêm/sửa/xóa ngành public</strong>
          </div>

          <div>
            <i class="fa-solid fa-newspaper"></i>
            <span>Tin tức</span>
            <strong>Quản lý bài viết public</strong>
          </div>

          <div>
            <i class="fa-solid fa-headset"></i>
            <span>Liên hệ tư vấn</span>
            <strong>Xem form người dùng gửi</strong>
          </div>
        </div>
      </div>

      <div class="content-right">
        <div class="note-box">
          <i class="fa-solid fa-circle-info"></i>

          <div>
            <h3>Ghi chú dữ liệu hiện tại</h3>

            <p>
              Giai đoạn này chưa có API backend cho module public.
              Dữ liệu admin được lưu tạm bằng localStorage của trình duyệt.
              F5 vẫn còn, nhưng đổi máy hoặc xóa cache thì dữ liệu sẽ mất.
            </p>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const danhSachChucNang = [
  {
    title: 'Banner',
    icon: 'fa-solid fa-image',
    to: {
      name: 'Admin.Public.BannerTrangChu'
    }
  },
  {
    title: 'Thông tin trường',
    icon: 'fa-solid fa-school',
    to: {
      name: 'Admin.Public.ThongTinTruong'
    }
  },
  {
    title: 'Mạng xã hội',
    icon: 'fa-solid fa-share-nodes',
    to: {
      name: 'Admin.Public.MangXaHoi'
    }
  },
  {
    title: 'Ngành đào tạo',
    icon: 'fa-solid fa-graduation-cap',
    to: {
      name: 'Admin.Public.NganhDaoTao'
    }
  },
  {
    title: 'Tin tức',
    icon: 'fa-solid fa-newspaper',
    to: {
      name: 'Admin.Public.TinTuc'
    }
  },
  {
    title: 'Tuyển sinh',
    icon: 'fa-solid fa-clipboard-list',
    to: {
      name: 'Admin.Public.TuyenSinh'
    }
  },
  {
    title: 'Cảm nhận',
    icon: 'fa-solid fa-comments',
    to: {
      name: 'Admin.Public.CamNhanHocVien'
    }
  },
  {
    title: 'Liên hệ',
    icon: 'fa-solid fa-headset',
    to: {
      name: 'Admin.Public.LienHe'
    }
  }
]
</script>

<style scoped>
.admin-public-page {
  display: grid;
  gap: 22px;
}

.admin-public-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
  padding: 28px;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background:
      radial-gradient(circle at top right, rgba(7, 95, 200, 0.12), transparent 32%),
      #ffffff;
  box-shadow: 0 10px 26px rgba(7, 63, 131, 0.06);
}

.eyebrow {
  margin: 0 0 8px;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.admin-public-header h1 {
  margin: 0 0 10px;
  color: #063b7a;
  font-size: 30px;
  font-weight: 900;
}

.admin-public-header p {
  max-width: 820px;
  margin: 0;
  color: #486581;
  line-height: 1.7;
}

.preview-button {
  min-height: 44px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  flex: 0 0 auto;
  padding: 0 18px;
  border-radius: 999px;
  background: #075fc8;
  color: #ffffff;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.admin-public-menu {
  display: flex;
  align-items: center;
  gap: 10px;
  overflow-x: auto;
  padding: 14px;
  border: 1px solid #dbe7f5;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.admin-public-menu::-webkit-scrollbar {
  height: 6px;
}

.admin-public-menu::-webkit-scrollbar-thumb {
  border-radius: 999px;
  background: #cfe1f6;
}

.admin-public-menu-item {
  min-height: 44px;
  display: inline-flex;
  align-items: center;
  gap: 9px;
  flex: 0 0 auto;
  padding: 0 18px;
  border: 1px solid #cfe1f6;
  border-radius: 999px;
  background: #f8fbff;
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
  transition: 0.2s ease;
}

.admin-public-menu-item i {
  color: #075fc8;
  font-size: 15px;
}

.admin-public-menu-item:hover {
  border-color: #075fc8;
  background: #eff6ff;
  color: #075fc8;
  transform: translateY(-2px);
}

.admin-public-menu-item.router-link-active,
.admin-public-menu-item.router-link-exact-active {
  border-color: #075fc8;
  background: #075fc8;
  color: #ffffff;
  box-shadow: 0 10px 22px rgba(7, 95, 200, 0.22);
}

.admin-public-menu-item.router-link-active i,
.admin-public-menu-item.router-link-exact-active i {
  color: #ffffff;
}

.admin-public-content {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 420px;
  gap: 22px;
}

.content-left,
.content-right {
  padding: 26px;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.content-left h2 {
  margin: 0 0 10px;
  color: #063b7a;
  font-size: 24px;
  font-weight: 900;
}

.content-left > p {
  max-width: 820px;
  margin: 0 0 22px;
  color: #486581;
  line-height: 1.7;
}

.quick-guide {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

.quick-guide div {
  display: grid;
  gap: 7px;
  padding: 18px;
  border: 1px solid #e6eef8;
  border-radius: 16px;
  background: #f8fbff;
}

.quick-guide i {
  width: 42px;
  height: 42px;
  display: grid;
  place-items: center;
  margin-bottom: 4px;
  border-radius: 14px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 18px;
}

.quick-guide span {
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.quick-guide strong {
  color: #607894;
  font-size: 13px;
  line-height: 1.45;
}

.note-box {
  display: grid;
  grid-template-columns: 44px 1fr;
  gap: 14px;
  padding: 20px;
  border: 1px solid #fde68a;
  border-radius: 18px;
  background: #fffbeb;
}

.note-box > i {
  width: 44px;
  height: 44px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #f59e0b;
  color: #ffffff;
}

.note-box h3 {
  margin: 0 0 8px;
  color: #92400e;
  font-size: 17px;
  font-weight: 900;
}

.note-box p {
  margin: 0;
  color: #92400e;
  font-size: 14px;
  line-height: 1.65;
}

@media (max-width: 1180px) {
  .admin-public-content {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .admin-public-header {
    display: grid;
  }

  .preview-button {
    justify-content: center;
  }

  .admin-public-menu {
    align-items: stretch;
  }

  .admin-public-menu-item {
    min-width: max-content;
  }

  .quick-guide {
    grid-template-columns: 1fr;
  }

  .content-left,
  .content-right {
    padding: 20px;
  }
}
</style>