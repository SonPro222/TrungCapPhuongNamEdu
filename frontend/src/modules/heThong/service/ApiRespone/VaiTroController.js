import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/vai-tro';

export const getAllVaiTro = (params = {}) => apiClient.get(ENDPOINT, { params });
export const getVaiTroById = (id) => apiClient.get(`${ENDPOINT}/${id}`);

export default { getAllVaiTro, getVaiTroById };
