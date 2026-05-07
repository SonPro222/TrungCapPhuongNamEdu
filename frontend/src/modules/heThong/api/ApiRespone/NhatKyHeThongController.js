import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/he-thong/nhat-ky-he-thong';

export const getAllNhatKyHeThong = (params = {}) => {
    return apiClient.get(ENDPOINT, { params });
};

export const getNhatKyHeThongById = (id) => {
    return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
    getAllNhatKyHeThong,
    getNhatKyHeThongById,
};