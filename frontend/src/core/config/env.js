export const ENV = {
  appName: import.meta.env.VITE_APP_NAME || 'Trung cấp Phương Nam',
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  apiTimeout: Number(import.meta.env.VITE_API_TIMEOUT || 30000),
  tokenKey: import.meta.env.VITE_TOKEN_KEY || 'accessToken',
  refreshTokenKey: import.meta.env.VITE_REFRESH_TOKEN_KEY || 'refreshToken',
  roleKey: import.meta.env.VITE_ROLE_KEY || 'role',
  locale: import.meta.env.VITE_LOCALE || 'vi',
  isDev: import.meta.env.DEV,
};

export const getEnv = (key, fallback = '') => import.meta.env[key] ?? fallback;
