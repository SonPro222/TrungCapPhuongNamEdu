<template>
  <section class="pn-home-page">
    <header class="pn-public-header">
      <RouterLink class="pn-public-brand" to="/">
        <span class="pn-logo-mark">PN</span>
        <span>
          <b>TRUNG CẤP PHƯƠNG NAM</b>
          <small>Education Management System</small>
        </span>
      </RouterLink>

      <nav class="pn-public-menu" aria-label="Liên kết nhanh">
        <a href="#gioi-thieu">Giới thiệu</a>
        <a href="#dang-nhap">Đăng nhập</a>
      </nav>
    </header>

    <main class="pn-home-main" id="gioi-thieu">
      <section class="pn-hero">
        <p class="pn-eyebrow">School Management Portal</p>
        <h1>Hệ thống quản lý đào tạo Trung cấp Phương Nam</h1>
        <p class="pn-hero-desc">
          Quản lý chương trình đào tạo, phiên bản, khung chương trình, môn học, điểm, sinh viên và các nghiệp vụ đào tạo trong một dashboard thống nhất.
        </p>

        <div class="pn-stat-strip">
          <article>
            <strong>Admin</strong>
            <span>Quản trị toàn bộ hệ thống</span>
          </article>
          <article>
            <strong>Đào tạo</strong>
            <span>Chương trình, môn học, khung đào tạo</span>
          </article>
          <article>
            <strong>Dữ liệu</strong>
            <span>Kết nối backend và database</span>
          </article>
        </div>
      </section>

      <aside class="pn-login-card" id="dang-nhap">
        <div class="pn-login-heading">
          <span class="pn-login-icon">PN</span>
          <div>
            <p>Đăng nhập hệ thống</p>
            <h2>Admin Portal</h2>
          </div>
        </div>

        <form class="pn-login-form" @submit.prevent="handleLogin">
          <label>
            <span>Tên đăng nhập / Email</span>
            <input v-model.trim="username" autocomplete="username" placeholder="Nhập tài khoản" />
          </label>

          <label>
            <span>Mật khẩu</span>
            <input v-model="password" type="password" autocomplete="current-password" placeholder="Nhập mật khẩu" />
          </label>

          <label class="pn-role-field">
            <span>Vai trò kiểm thử</span>
            <select v-model="role">
              <option value="ADMIN">Admin / Quản trị</option>
              <option value="GIAO_VU">Giáo vụ đào tạo</option>
              <option value="GIANG_VIEN">Giảng viên</option>
              <option value="SINH_VIEN">Sinh viên</option>
            </select>
          </label>

          <p v-if="error" class="pn-error-text">{{ error }}</p>

          <button class="pn-login-button" type="submit" :disabled="loading">
            {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
          </button>

          <button class="pn-demo-button" type="button" @click="loginDemo">
            Vào nhanh bằng tài khoản demo
          </button>
        </form>
      </aside>
    </main>
  </section>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/core/api/apiClient';
import pageURL from '@/core/config/pageURL';
import { getHomeByRole, normalizeRole } from '@/utils/permission.js';

const router = useRouter();
const username = ref('');
const password = ref('');
const role = ref('ADMIN');
const loading = ref(false);
const error = ref('');

const pickRole = (data = {}) => {
  const candidate = data.role || data.vaiTro || data.roleName || data.authority || data.quyen;
  if (candidate) return candidate;
  if (Array.isArray(data.roles) && data.roles.length) return data.roles[0];
  if (Array.isArray(data.vaiTros) && data.vaiTros.length) return data.vaiTros[0]?.tenVaiTro || data.vaiTros[0];
  return role.value;
};

const persistLogin = ({ token, userRole, user }) => {
  const normalizedRole = normalizeRole(userRole || role.value);
  localStorage.setItem('accessToken', token || 'demo-token');
  localStorage.setItem('role', normalizedRole);
  localStorage.setItem('user', JSON.stringify(user || { username: username.value || 'demo', role: normalizedRole }));
  router.replace(getHomeByRole(normalizedRole));
};

const handleLogin = async () => {
  error.value = '';

  if (!username.value || !password.value) {
    error.value = 'Vui lòng nhập tài khoản và mật khẩu.';
    return;
  }

  loading.value = true;

  try {
    const response = await apiClient.post(`${pageURL.AUTH}/login`, {
      username: username.value,
      email: username.value,
      tenDangNhap: username.value,
      password: password.value,
      matKhau: password.value,
    });

    const payload = response?.data?.data ?? response?.data?.result ?? response?.data ?? {};
    const user = payload.user || payload.taiKhoan || payload;

    persistLogin({
      token: payload.accessToken || payload.token || payload.jwt,
      userRole: pickRole(user) || pickRole(payload),
      user,
    });
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Không đăng nhập được. Vui lòng kiểm tra backend.';
  } finally {
    loading.value = false;
  }
};

const loginDemo = () => {
  persistLogin({
    token: 'demo-token',
    userRole: role.value,
    user: { username: username.value || 'admin.demo', demo: true, role: role.value },
  });
};
</script>

<style scoped>
.pn-home-page{min-height:100vh;background:#f3f5f8;color:#172033}.pn-public-header{height:82px;display:flex;align-items:center;justify-content:space-between;gap:24px;padding:0 7vw;border-bottom:1px solid #d9dee8;background:#fff}.pn-public-brand{display:inline-flex;align-items:center;gap:13px;color:#172033;text-decoration:none}.pn-logo-mark{width:46px;height:46px;border-radius:12px;background:linear-gradient(135deg,#2454d8,#ff7a1a);color:#fff;display:inline-flex;align-items:center;justify-content:center;font-size:16px;font-weight:900}.pn-public-brand b{display:block;font-size:18px;letter-spacing:.01em}.pn-public-brand small{display:block;margin-top:3px;color:#64748b;font-size:12px}.pn-public-menu{display:flex;align-items:center;gap:28px}.pn-public-menu a{color:#334155;text-decoration:none;font-size:15px;font-weight:800}.pn-public-menu a:hover{color:#2454d8}.pn-home-main{display:grid;grid-template-columns:minmax(0,1.2fr) 450px;gap:48px;align-items:center;min-height:calc(100vh - 82px);padding:54px 7vw}.pn-hero{display:grid;gap:22px}.pn-eyebrow{margin:0;color:#2454d8;font-size:13px;font-weight:900;text-transform:uppercase;letter-spacing:.12em}.pn-hero h1{max-width:860px;margin:0;color:#1f2937;font-size:58px;line-height:1.04;letter-spacing:-.055em;font-weight:950}.pn-hero-desc{max-width:780px;margin:0;color:#334155;font-size:19px;line-height:1.65}.pn-stat-strip{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:1px;margin-top:18px;max-width:920px;border-radius:18px;overflow:hidden;background:#111827;color:#fff}.pn-stat-strip article{display:grid;gap:5px;min-height:104px;padding:22px 26px}.pn-stat-strip strong{font-size:25px;line-height:1.2}.pn-stat-strip span{font-size:15px;line-height:1.35;color:#e5e7eb}.pn-login-card{border:1px solid #d8e0ed;border-radius:22px;background:#fff;padding:30px;box-shadow:0 28px 70px rgba(15,23,42,.14)}.pn-login-heading{display:flex;align-items:center;gap:14px;margin-bottom:22px}.pn-login-icon{width:50px;height:50px;border-radius:14px;background:#eff6ff;color:#1d4ed8;display:flex;align-items:center;justify-content:center;font-size:17px;font-weight:950}.pn-login-heading p{margin:0 0 3px;color:#64748b;font-size:13px;font-weight:850;text-transform:uppercase}.pn-login-heading h2{margin:0;color:#172033;font-size:25px}.pn-login-form{display:grid;gap:15px}.pn-login-form label{display:grid;gap:7px}.pn-login-form span{font-size:14px;font-weight:850;color:#334155}.pn-login-form input,.pn-login-form select{height:46px;border:1px solid #cbd5e1;border-radius:11px;background:#fff;padding:0 13px;color:#111827;font-size:15px;outline:none}.pn-login-form input:focus,.pn-login-form select:focus{border-color:#2454d8;box-shadow:0 0 0 4px rgba(36,84,216,.12)}.pn-login-button,.pn-demo-button{height:46px;border:0;border-radius:11px;font-size:15px;font-weight:900;cursor:pointer}.pn-login-button{margin-top:4px;background:#2454d8;color:#fff}.pn-login-button:disabled{opacity:.7;cursor:not-allowed}.pn-demo-button{background:#f1f5f9;color:#172033}.pn-error-text{margin:0;border-radius:10px;background:#fef2f2;color:#b91c1c;padding:11px 12px;font-size:13px;font-weight:800}@media(max-width:1050px){.pn-home-main{grid-template-columns:1fr}.pn-hero h1{font-size:44px}.pn-login-card{max-width:560px}}@media(max-width:720px){.pn-public-header{height:auto;align-items:flex-start;flex-direction:column;padding:16px}.pn-public-menu{gap:14px;flex-wrap:wrap}.pn-home-main{padding:30px 16px}.pn-hero h1{font-size:34px}.pn-hero-desc{font-size:16px}.pn-stat-strip{grid-template-columns:1fr}}
</style>
