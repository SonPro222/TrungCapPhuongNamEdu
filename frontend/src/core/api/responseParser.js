export function parseResponse(response) {
    const body = response?.data

    if (body && typeof body === 'object' && 'success' in body) {
        if (!body.success) {
            throw {
                status: response?.status || 400,
                message: body.message || 'Thao tác thất bại',
                data: body.data
            }
        }

        return body.data
    }

    return body
}

export function parseError(error) {
    const status = error?.response?.status || error?.status || 500
    const data = error?.response?.data

    return {
        status,
        message:
            data?.message ||
            data?.error ||
            error?.message ||
            'Có lỗi xảy ra',
        data
    }
}