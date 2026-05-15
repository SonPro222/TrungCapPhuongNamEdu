<template>
  <div class="pn-app-shell" :class="{ collapsed }">
    <aside class="pn-work-rail" aria-label="Điều hướng chính">
      <RouterLink class="pn-brand" to="/he-thong" aria-label="Phương Nam">
        <span>PN</span>
        <b v-if="!collapsed">Phương Nam</b>
      </RouterLink>

      <button class="pn-collapse" type="button" :aria-label="collapsed ? 'Mở rộng' : 'Thu gọn'" @click="collapsed = !collapsed">
        {{ collapsed ? '›' : '‹' }}
      </button>

      <nav class="pn-rail-nav">
        <div
          v-for="item in dieuHuong"
          :key="item.key"
          class="pn-rail-group"
          @mouseenter="openSection(item.key)"
          @mouseleave="closeSection"
        >
          <RouterLink class="pn-rail-link" :to="item.to">
            <span class="pn-nav-icon">{{ item.icon }}</span>
            <span v-if="!collapsed" class="pn-nav-text">
              <b>{{ item.ten }}</b>
              <small>{{ item.moTa }}</small>
            </span>
          </RouterLink>

          <div v-if="hoverSection === item.key" class="pn-flyout" :class="`is-${item.key}`">
            <template v-if="item.key === 'he-thong'">
              <section class="pn-flyout-panel pn-panel-main">
                <header>
                  <b>Hệ thống</b>
                  <span>Di chuột để xem cấu trúc</span>
                </header>
                <button
                  v-for="module in moduleList"
                  :key="module.key"
                  type="button"
                  :class="['pn-flyout-row', { active: activeModuleKey === module.key }]"
                  @mouseenter="activeModuleKey = module.key; activeGroupKey = module.nhom?.[0]?.key || ''"
                >
                  <span>
                    <b>{{ module.ten }}</b>
                    <small>{{ module.moTa }}</small>
                  </span>
                  <em v-if="module.nhom?.length">›</em>
                </button>
              </section>

              <section v-if="activeModule?.nhom?.length" class="pn-flyout-panel pn-panel-group">
                <header>
                  <b>{{ activeModule.ten }}</b>
                  <span>Chọn nhóm</span>
                </header>
                <button
                  v-for="group in activeModule.nhom"
                  :key="group.key"
                  type="button"
                  :class="['pn-flyout-row', { active: activeGroupKey === group.key }]"
                  @mouseenter="activeGroupKey = group.key"
                >
                  <span>
                    <b>{{ group.ten }}</b>
                    <small>{{ group.moTa }}</small>
                  </span>
                  <em>›</em>
                </button>
              </section>

              <section v-if="activeGroup" class="pn-flyout-panel pn-panel-item">
                <header>
                  <b>{{ activeGroup.ten }}</b>
                  <span>Click để mở dữ liệu</span>
                </header>
                <RouterLink
                  v-for="child in activeGroup.muc"
                  :key="child.segment"
                  class="pn-flyout-link"
                  :to="child.duLieu"
                  @click="closeSection"
                >
                  {{ child.ten }}
                </RouterLink>
              </section>
            </template>

            <template v-else>
              <section class="pn-flyout-panel pn-simple-panel">
                <header>
                  <b>{{ item.ten }}</b>
                  <span>{{ item.key === 'quy-trinh' ? 'Click để làm việc' : 'Click để quản lý dữ liệu' }}</span>
                </header>
                <RouterLink
                  v-for="module in moduleList"
                  :key="`${item.key}-${module.key}`"
                  class="pn-flyout-row link"
                  :to="resolveModuleLink(item.key, module)"
                  @click="closeSection"
                >
                  <span>
                    <b>{{ module.ten }}</b>
                    <small>{{ module.moTa }}</small>
                  </span>
                </RouterLink>
              </section>
            </template>
          </div>
        </div>
      </nav>

      <div class="pn-user" v-if="!collapsed">
        <span class="pn-avatar">{{ userInitials }}</span>
        <span>
          <b>{{ userEmail }}</b>
          <small>{{ userRole }}</small>
        </span>
      </div>
      <button class="pn-exit" type="button" @click="handleLogout">
        <span>⇱</span>
        <b v-if="!collapsed">Thoát</b>
      </button>
    </aside>

    <main class="pn-work-area">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { clearAuthStorage } from '@/utils/permission.js';
import { DIEU_HUONG_CHINH, PHAN_HE_HE_THONG } from '@/shared/config/khongGianLamViec.js';

const router = useRouter();
const collapsed = ref(false);
const hoverSection = ref('');
const activeModuleKey = ref('chuong-trinh');
const activeGroupKey = ref('danh-muc');
const dieuHuong = DIEU_HUONG_CHINH;
const moduleList = PHAN_HE_HE_THONG;

const userEmail = computed(() => localStorage.getItem('email') || localStorage.getItem('username') || 'admin@gmail.com');
const userRole = computed(() => localStorage.getItem('role') || 'ADMIN');
const userInitials = computed(() => String(userEmail.value).slice(0, 2).toUpperCase());

const activeModule = computed(() => moduleList.find((module) => module.key === activeModuleKey.value) || moduleList[0]);
const activeGroup = computed(() => activeModule.value?.nhom?.find((group) => group.key === activeGroupKey.value) || activeModule.value?.nhom?.[0]);

const openSection = (key) => {
  hoverSection.value = key;
  if (key === 'he-thong') {
    activeModuleKey.value = activeModuleKey.value || 'chuong-trinh';
    activeGroupKey.value = activeGroup.value?.key || 'danh-muc';
  }
};

const closeSection = () => {
  hoverSection.value = '';
};

const resolveModuleLink = (section, module) => {
  if (section === 'quy-trinh') return module.quyTrinh || '/quy-trinh';
  if (section === 'du-lieu') return module.duLieu || '/du-lieu';
  return module.quyTrinh || '/he-thong';
};

const handleLogout = () => {
  clearAuthStorage();
  router.replace('/login');
};
</script>

<style scoped>
.pn-app-shell{--rail-width:248px;width:100vw;height:100vh;display:grid;grid-template-columns:var(--rail-width) minmax(0,1fr);overflow:hidden;background:#f5f7fb;color:#101828;font-family:Inter,"Plus Jakarta Sans",system-ui,-apple-system,BlinkMacSystemFont,"Segoe UI",sans-serif}.pn-app-shell.collapsed{--rail-width:76px}.pn-work-rail{position:relative;z-index:40;background:#0f1724;border-right:1px solid #101828;padding:12px 10px;display:flex;flex-direction:column;gap:14px}.pn-brand{height:48px;border-radius:13px;background:#2563eb;color:#fff;text-decoration:none;display:flex;align-items:center;gap:10px;padding:0 12px;font-weight:900;box-shadow:0 10px 24px rgba(37,99,235,.22)}.pn-brand span{width:31px;height:31px;border-radius:10px;background:rgba(255,255,255,.16);display:grid;place-items:center}.pn-brand b{font-size:14px;white-space:nowrap}.pn-collapse{position:absolute;right:-15px;top:50%;transform:translateY(-50%);z-index:70;width:30px;height:52px;border-radius:999px;border:1px solid #334155;background:#111827;color:#fff;font-size:22px;font-weight:900;cursor:pointer;box-shadow:0 12px 28px rgba(15,23,42,.25)}.pn-rail-nav{display:grid;gap:8px}.pn-rail-group{position:relative}.pn-rail-link{min-height:58px;border-radius:13px;color:#d8e0ec;text-decoration:none;display:flex;align-items:center;gap:12px;padding:8px 10px;border:1px solid transparent}.pn-nav-icon{width:32px;height:32px;border-radius:10px;background:#172235;display:grid;place-items:center;font-size:17px;color:#e2e8f0;flex:0 0 auto}.pn-nav-text{display:grid;gap:2px;min-width:0}.pn-nav-text b{font-size:14px;line-height:1.1;font-weight:850;white-space:nowrap}.pn-nav-text small{font-size:11.5px;color:#93a3b8;white-space:nowrap}.pn-rail-link:hover,.pn-rail-link.router-link-active{background:#1d2939;color:#fff;border-color:#344054}.pn-rail-link.router-link-active .pn-nav-icon,.pn-rail-link:hover .pn-nav-icon{background:#2563eb;color:#fff}.collapsed .pn-rail-link{justify-content:center;padding:8px}.collapsed .pn-brand{justify-content:center;padding:0}.pn-flyout{position:absolute;left:calc(100% + 12px);top:0;display:flex;align-items:flex-start;gap:12px;z-index:80}.pn-flyout:before{content:"";position:absolute;left:-18px;top:0;width:20px;height:100%}.pn-flyout-panel{width:310px;max-height:calc(100vh - 36px);overflow:auto;border:1px solid #d9e2ef;border-radius:18px;background:#fff;box-shadow:0 28px 80px rgba(15,23,42,.18);padding:12px}.pn-panel-group{width:290px}.pn-panel-item{width:280px}.pn-simple-panel{width:330px}.pn-flyout-panel header{padding:6px 6px 12px;border-bottom:1px solid #eef2f7;display:grid;gap:4px;margin-bottom:8px}.pn-flyout-panel header b{font-size:17px;letter-spacing:-.02em;color:#101828}.pn-flyout-panel header span{font-size:12.5px;color:#667085}.pn-flyout-row,.pn-flyout-link{width:100%;border:0;background:transparent;text-align:left;text-decoration:none;color:#1d2939;border-radius:12px;padding:11px;display:flex;align-items:center;justify-content:space-between;gap:12px;cursor:pointer}.pn-flyout-row span{display:grid;gap:4px;min-width:0}.pn-flyout-row b{font-size:13.5px}.pn-flyout-row small{font-size:11.5px;color:#667085;line-height:1.35;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden}.pn-flyout-row em{font-style:normal;color:#98a2b3}.pn-flyout-row:hover,.pn-flyout-row.active,.pn-flyout-link:hover{background:#eff6ff;color:#1d4ed8}.pn-flyout-row.link{display:flex}.pn-flyout-link{font-weight:780;font-size:13.5px}.pn-user{margin-top:auto;border:1px solid #334155;border-radius:13px;background:#111c2d;padding:10px;display:flex;align-items:center;gap:10px;color:#fff}.pn-avatar{width:34px;height:34px;border-radius:10px;background:#e0edff;color:#1d4ed8;display:grid;place-items:center;font-size:12px;font-weight:900}.pn-user span:last-child{display:grid;gap:2px;min-width:0}.pn-user b{font-size:12.5px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.pn-user small{font-size:11px;color:#cbd5e1}.pn-exit{height:38px;border:1px solid #334155;border-radius:11px;background:#182230;color:#e5e7eb;font-size:12px;font-weight:850;cursor:pointer;display:flex;align-items:center;justify-content:center;gap:8px}.pn-exit:hover{background:#243044}.pn-work-area{min-width:0;min-height:0;overflow:auto;background:#f5f7fb}.collapsed .pn-user{display:none}.collapsed .pn-exit b{display:none}@media(max-width:820px){.pn-app-shell,.pn-app-shell.collapsed{grid-template-columns:1fr}.pn-work-rail{position:sticky;top:0;display:grid;grid-template-columns:48px 1fr 40px;align-items:center}.pn-collapse,.pn-flyout{display:none}.pn-rail-nav{grid-template-columns:repeat(3,minmax(0,1fr))}.pn-rail-link{min-height:44px}.pn-nav-text small{display:none}.pn-user{display:none}.pn-exit{height:38px}.pn-work-area{height:calc(100vh - 72px)}}
</style>
