import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/nang-luc-dau-ra';

export const getAllNangLucDauRa = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getNangLucDauRaById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllNangLucDauRa,
  getNangLucDauRaById,
};
