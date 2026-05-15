import { ENV } from './env.js';

export const APP_CONFIG = {
  name: ENV.appName,
  schoolName: 'Trung cấp Phương Nam',
  version: import.meta.env.VITE_APP_VERSION || '1.0.0',
  defaultLocale: ENV.locale,
  defaultPageSize: 10,
  pageSizeOptions: [10, 20, 50, 100],
  api: {
    baseUrl: ENV.apiBaseUrl,
    timeout: ENV.apiTimeout,
  },
};

export default APP_CONFIG;
