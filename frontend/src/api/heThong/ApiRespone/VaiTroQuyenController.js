import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/vai-tro-quyen';

export const getAllVaiTroQuyen = (params = {}) => apiClient.get(ENDPOINT, { params });
export const getVaiTroQuyenById = (id) => apiClient.get(`${ENDPOINT}/${id}`);

export default { getAllVaiTroQuyen, getVaiTroQuyenById };
