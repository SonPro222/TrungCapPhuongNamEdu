import { computed, reactive } from 'vue';
import tokenService from '@/core/services/tokenService.js';
import { normalizeRole } from '@/utils/permission.js';

const state = reactive({
  user: null,
  token: tokenService.getAccessToken(),
  role: normalizeRole(tokenService.getRole()),
});

export const useAuthStore = () => {
  const hydrate = () => {
    state.token = tokenService.getAccessToken();
    state.role = normalizeRole(tokenService.getRole());
    try {
      state.user = JSON.parse(localStorage.getItem('user') || 'null');
    } catch {
      state.user = null;
    }
  };

  const setAuth = ({ user, accessToken, role } = {}) => {
    if (accessToken) tokenService.setAccessToken(accessToken);
    if (role) tokenService.setRole(role);
    if (user) localStorage.setItem('user', JSON.stringify(user));
    hydrate();
  };

  const clearAuth = () => {
    tokenService.clear();
    state.user = null;
    state.token = '';
    state.role = '';
  };

  hydrate();

  return {
    state,
    user: computed(() => state.user),
    token: computed(() => state.token),
    role: computed(() => state.role),
    isAuthenticated: computed(() => Boolean(state.token)),
    hydrate,
    setAuth,
    clearAuth,
  };
};

export default useAuthStore;
