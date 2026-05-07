import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/he-thong/nhat-ky-dang-nhap';

export const getAllNhatKyDangNhap = (params = {}) => {
    return apiClient.get(ENDPOINT, { params });
};

export const getNhatKyDangNhapById = (id) => {
    return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
    getAllNhatKyDangNhap,
    getNhatKyDangNhapById,
};