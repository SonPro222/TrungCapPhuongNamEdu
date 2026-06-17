import axios from 'axios'
import { ENV } from '../config/env'
import { tokenService } from '../services/tokenService'
import { parseResponse, parseError } from './responseParser'

function redirectToLogin() {
    if (window.location.pathname !== '/auth/login') {
        window.location.href = '/auth/login'
    }
}

function isPublicAuthUrl(url = '') {
    return (
        url.includes('/auth/login') ||
        url.includes('/auth/google')
    )
}

const apiClient = axios.create({
    baseURL: ENV.API_BASE_URL,
    timeout: 30000,
    headers: {
        'Content-Type': 'application/json'
    }
})

apiClient.interceptors.request.use(
    (config) => {
        const token = tokenService.getToken()

        // API login/google không cần token
        if (config.skipAuth || isPublicAuthUrl(config.url)) {
            return config
        }

        if (!token) {
            tokenService.clearAuth()
            redirectToLogin()

            return Promise.reject({
                status: 401,
                message: 'Bạn chưa đăng nhập.'
            })
        }

        if (tokenService.isTokenExpired()) {
            tokenService.clearAuth()
            redirectToLogin()

            return Promise.reject({
                status: 401,
                message: 'Phiên đăng nhập đã hết hạn.'
            })
        }

        config.headers.Authorization = `Bearer ${token}`

        return config
    },
    (error) => Promise.reject(parseError(error))
)

apiClient.interceptors.response.use(
    (response) => parseResponse(response),
    (error) => {
        const parsedError = parseError(error)

        if (parsedError.status === 401) {
            tokenService.clearAuth()
            redirectToLogin()
        }

        return Promise.reject(parsedError)
    }
)

export default apiClient