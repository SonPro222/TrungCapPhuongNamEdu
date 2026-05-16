import { ref } from 'vue'

export function useCrudApi(service) {
    const items = ref([])
    const currentItem = ref(null)
    const loading = ref(false)
    const error = ref(null)

    async function fetchAll(params = {}) {
        loading.value = true
        error.value = null

        try {
            items.value = await service.getAll(params)
        } catch (err) {
            error.value = err
        } finally {
            loading.value = false
        }
    }

    async function fetchById(id) {
        loading.value = true
        error.value = null

        try {
            currentItem.value = await service.getById(id)
        } catch (err) {
            error.value = err
        } finally {
            loading.value = false
        }
    }

    async function createItem(payload) {
        return service.create(payload)
    }

    async function updateItem(id, payload) {
        return service.update(id, payload)
    }

    async function deleteItem(id) {
        return service.delete(id)
    }

    return {
        items,
        currentItem,
        loading,
        error,
        fetchAll,
        fetchById,
        createItem,
        updateItem,
        deleteItem
    }
}