import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/tai-khoan';

export const getAllTaiKhoan = (params = {}) => apiClient.get(ENDPOINT, { params });
export const getPageTaiKhoan = (params = {}) => apiClient.get(`${ENDPOINT}/page`, { params });
export const getTaiKhoanById = (id) => apiClient.get(`${ENDPOINT}/${id}`);

export default { getAllTaiKhoan, getPageTaiKhoan, getTaiKhoanById };
