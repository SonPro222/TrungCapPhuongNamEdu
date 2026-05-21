<template>
  <nav class="chuong-trinh-menu" aria-label="Điều hướng module chương trình">
    <RouterLink to="/admin/chuong-trinh/xay-dung">Xây dựng chương trình</RouterLink>
    <RouterLink to="/admin/chuong-trinh/dao-tao-xem">Đào tạo xem</RouterLink>
  </nav>
</template>
<style scoped>.chuong-trinh-menu{display:flex;flex-wrap:wrap;gap:10px}.chuong-trinh-menu a{padding:8px 12px;border:1px solid var(--color-border);border-radius:var(--radius);background:var(--color-white);color:var(--color-text);text-decoration:none}.chuong-trinh-menu a.router-link-active{background:var(--color-primary);color:#fff;border-color:var(--color-primary)}</style>
