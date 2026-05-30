function themNeuCo(list, value) {
    if (value === null || value === undefined) return
    if (Array.isArray(value)) {
        value.forEach((item) => themNeuCo(list, item))
        return
    }
    if (typeof value === 'object') {
        if (value.message) themNeuCo(list, value.message)
        if (value.error) themNeuCo(list, value.error)
        if (value.defaultMessage) themNeuCo(list, value.defaultMessage)
        if (value.reason) themNeuCo(list, value.reason)
        if (value.errors) themNeuCo(list, value.errors)
        if (value.data) themNeuCo(list, value.data)
        return
    }
    const text = String(value).trim()
    if (text) list.push(text)
}

export function layDanhSachThongBaoLoi(error) {
    const messages = []

    if (typeof error === 'string') {
        themNeuCo(messages, error)
        return messages
    }

    const responseData = error?.response?.data
    const data = error?.data

    themNeuCo(messages, responseData?.message)
    themNeuCo(messages, responseData?.error)
    themNeuCo(messages, responseData?.errors)
    themNeuCo(messages, responseData?.data)

    themNeuCo(messages, data?.message)
    themNeuCo(messages, data?.error)
    themNeuCo(messages, data?.errors)
    themNeuCo(messages, data?.data)

    themNeuCo(messages, error?.message)
    themNeuCo(messages, error?.error)

    return [...new Set(messages.filter(Boolean))]
}

export function layThongBaoLoi(error, fallback = 'Thao tác thất bại. Vui lòng kiểm tra dữ liệu.') {
    const messages = layDanhSachThongBaoLoi(error)
    return messages.length ? messages.join('\n') : fallback
}
