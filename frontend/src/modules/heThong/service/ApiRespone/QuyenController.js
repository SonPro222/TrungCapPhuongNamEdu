import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/quyen';

export const getAllQuyen = (params = {}) => apiClient.get(ENDPOINT, { params });
export const getQuyenById = (id) => apiClient.get(`${ENDPOINT}/${id}`);

export default { getAllQuyen, getQuyenById };
