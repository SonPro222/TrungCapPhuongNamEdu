import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/sinh-vien';

export const getAllSinhVien = (params = {}) => {
    return apiClient.get(ENDPOINT, { params });
};

export const getSinhVienById = (id) => {
    return apiClient.get(`${ENDPOINT}/${id}`);
};

export const getPageSinhVien = (params = {}) => {
    return apiClient.get(`${ENDPOINT}`, { params });
};

export default {
    getAllSinhVien,
    getSinhVienById,
    getPageSinhVien,
};