import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authModuleService } from '../services/authModuleService'

export function useAuth() {
    const router = useRouter()

    const loading = ref(false)
    const errorMessage = ref('')

    async function login(form) {
        loading.value = true
        errorMessage.value = ''

        try {
            const auth = await authModuleService.login(form)
            router.replace(auth.homePath || '/admin')
        } catch (error) {
            console.error('LOGIN_ERROR:', error)
            errorMessage.value = error?.message || 'Đăng nhập thất bại'
        } finally {
            loading.value = false
        }
    }

    function logout() {
        authModuleService.logout()
        router.replace('/auth/login')
    }

    return {
        loading,
        errorMessage,
        login,
        logout
    }
}