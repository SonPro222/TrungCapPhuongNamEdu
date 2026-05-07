import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/lan-lam-bai-online';

export const createLanLamBaiOnline = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateLanLamBaiOnline = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteLanLamBaiOnline = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createLanLamBaiOnline,
  updateLanLamBaiOnline,
  deleteLanLamBaiOnline,
};
