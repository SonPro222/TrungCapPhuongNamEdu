import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/he-thong/nhan-vien';

export const getAllNhanVien = (params = {}) => apiClient.get(ENDPOINT, { params });
export const getNhanVienById = (id) => apiClient.get(`${ENDPOINT}/${id}`);

export default { getAllNhanVien, getNhanVienById };
