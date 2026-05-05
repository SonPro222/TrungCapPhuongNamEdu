import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/tai-khoan-vai-tro';

export const getAllTaiKhoanVaiTro = (params = {}) => apiClient.get(ENDPOINT, { params });
export const getTaiKhoanVaiTroById = (id) => apiClient.get(`${ENDPOINT}/${id}`);

export default { getAllTaiKhoanVaiTro, getTaiKhoanVaiTroById };
