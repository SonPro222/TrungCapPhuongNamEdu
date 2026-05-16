import { reactive, ref } from 'vue'

export function useDaoTaoCrud(service, defaultForm) {
    const items = ref([])
    const loading = ref(false)
    const saving = ref(false)
    const errorMessage = ref('')
    const editingId = ref(null)

    const pagination = reactive({
        page: 0,
        size: 10,
        totalElements: 0,
        totalPages: 0
    })

    const form = reactive({ ...defaultForm })

    function resetForm() {
        Object.assign(form, defaultForm)
        editingId.value = null
        errorMessage.value = ''
    }

    async function fetchItems() {
        loading.value = true
        errorMessage.value = ''

        try {
            const res = await service.getAll({
                page: pagination.page,
                size: pagination.size
            })

            items.value = res.items
            pagination.totalElements = res.totalElements
            pagination.totalPages = res.totalPages
            pagination.page = res.page
            pagination.size = res.size
        } catch (error) {
            console.error('FETCH_DAO_TAO_ERROR:', error)
            errorMessage.value = error?.message || 'Không tải được dữ liệu'
        } finally {
            loading.value = false
        }
    }

    function editItem(item) {
        Object.assign(form, item)
        editingId.value = item.id
    }

    async function saveItem() {
        saving.value = true
        errorMessage.value = ''

        try {
            if (editingId.value) {
                await service.update(editingId.value, form)
            } else {
                await service.create(form)
            }

            resetForm()
            await fetchItems()
        } catch (error) {
            errorMessage.value = error?.message || 'Lưu dữ liệu thất bại'
        } finally {
            saving.value = false
        }
    }

    async function deleteItem(id) {
        if (!confirm('Bạn có chắc muốn xóa dữ liệu này?')) return

        try {
            await service.delete(id)
            await fetchItems()
        } catch (error) {
            errorMessage.value = error?.message || 'Xóa dữ liệu thất bại'
        }
    }

    return {
        items,
        form,
        loading,
        saving,
        errorMessage,
        editingId,
        pagination,
        fetchItems,
        editItem,
        saveItem,
        deleteItem,
        resetForm
    }
}