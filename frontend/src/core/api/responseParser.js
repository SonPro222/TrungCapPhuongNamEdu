export function parseResponse(response) {
    const body = response?.data

    if (body && typeof body === 'object' && 'success' in body) {
        if (!body.success) {
            throw {
                status: response?.status || 400,
                message: chuanHoaThongBaoLoi(body.message || 'Thao tác thất bại'),
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
        message: chuanHoaThongBaoLoi(
            data?.message ||
            data?.error ||
            error?.message ||
            'Có lỗi xảy ra'
        ),
        data
    }
}

function chuanHoaThongBaoLoi(message) {
    const text = String(message || '').trim()

    if (!text) {
        return 'Có lỗi xảy ra, vui lòng thử lại'
    }

    if (text.includes('No static resource')) {
        const path = text
            .replace('No static resource', '')
            .replace('.', '')
            .trim()

        return path
            ? `Đường dẫn API không tồn tại: /${path.replace(/^\/+/, '')}`
            : 'Đường dẫn API không tồn tại'
    }

    if (text.includes('Failed to convert value') || text.includes('MethodArgumentTypeMismatch')) {
        return 'Dữ liệu gửi lên không đúng định dạng. Vui lòng kiểm tra lại thông tin đã chọn.'
    }

    if (text.includes('Required request parameter') || text.includes('MissingServletRequestParameter')) {
        return 'Thiếu tham số bắt buộc. Vui lòng kiểm tra lại dữ liệu gửi lên.'
    }

    if (text.includes('Maximum upload size exceeded') || text.includes('MaxUploadSizeExceeded')) {
        return 'File upload vượt quá dung lượng cho phép.'
    }

    if (text.includes('Network Error')) {
        return 'Không kết nối được máy chủ. Vui lòng kiểm tra BE đã chạy chưa.'
    }

    if (text.startsWith('SYSTEM_ERROR:')) {
        return text.replace('SYSTEM_ERROR:', '').trim() || 'Hệ thống đang gặp lỗi. Vui lòng thử lại.'
    }

    return text
}
