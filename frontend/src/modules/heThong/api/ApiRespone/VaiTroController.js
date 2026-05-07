import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/he-thong/vai-tro';

export const getAllVaiTro = (params = {}) => apiClient.get(ENDPOINT, { params });
export const getVaiTroById = (id) => apiClient.get(`${ENDPOINT}/${id}`);

export default { getAllVaiTro, getVaiTroById };
