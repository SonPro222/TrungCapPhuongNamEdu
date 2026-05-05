import axios from 'axios';

export const BASE_URL = 'http://localhost:8080';

export const GOOGLE_LOGIN_URL = `${BASE_URL}/oauth2/authorization/google`;

export const loginGoogle = () => {
    window.location.href = GOOGLE_LOGIN_URL;
};

const apiClient = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
    },
    timeout: 30000,
    withCredentials: false,
});

apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken') || localStorage.getItem('token');

        if (token) {
            config.headers = config.headers || {};
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
    (response) => response.data,
    (error) => {
        const status = error?.response?.status;

        if (status === 401) {
            localStorage.removeItem('accessToken');
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            localStorage.removeItem('role');
        }

        const apiError = {
            status,
            message:
                error?.response?.data?.message ||
                error?.response?.data?.error ||
                error?.message ||
                'Có lỗi xảy ra khi gọi API',
            data: error?.response?.data,
        };

        return Promise.reject(apiError);
    }
);

export default apiClient;