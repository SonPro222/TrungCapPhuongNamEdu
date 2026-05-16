import apiClient from '../../../core/api/apiClient'

export const authApi = {
    login(payload) {
        return apiClient.post('/auth/login', payload, {
            skipAuth: true
        })
    },

    logout() {
        return apiClient.post('/auth/logout')
    },

    getProfile() {
        return apiClient.get('/auth/profile')
    }
}