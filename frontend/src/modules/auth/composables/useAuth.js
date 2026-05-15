import { computed, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import authService from '@/modules/auth/services/authService.js';
import tokenService from '@/core/services/tokenService.js';
import { getHomeByRole, normalizeRole } from '@/utils/permission.js';

export const useAuth = () => {
  const router = useRouter();
  const loading = ref(false);
  const error = ref('');
  const form = reactive({ username: '', password: '' });

  const role = computed(() => normalizeRole(tokenService.getRole()));
  const isAuthenticated = computed(() => Boolean(tokenService.getAccessToken()));

  const login = async (credentials = form) => {
    loading.value = true;
    error.value = '';
    try {
      const auth = await authService.login(credentials);
      await router.replace(getHomeByRole(auth.role));
      return auth;
    } catch (err) {
      error.value = err?.response?.data?.message || err?.message || 'Đăng nhập không thành công.';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const logout = async () => {
    await authService.logout();
    await router.replace('/login');
  };

  return { error, form, isAuthenticated, loading, login, logout, role };
};

export default useAuth;
