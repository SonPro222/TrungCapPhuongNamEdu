import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/nang-luc-dau-ra';

export const createNangLucDauRa = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateNangLucDauRa = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteNangLucDauRa = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createNangLucDauRa,
  updateNangLucDauRa,
  deleteNangLucDauRa,
};
