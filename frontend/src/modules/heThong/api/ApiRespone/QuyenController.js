import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/he-thong/quyen';

export const getAllQuyen = (params = {}) => apiClient.get(ENDPOINT, { params });
export const getQuyenById = (id) => apiClient.get(`${ENDPOINT}/${id}`);

export default { getAllQuyen, getQuyenById };
