import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/chuong-trinh-version';

export const createChuongTrinhVersion = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateChuongTrinhVersion = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteChuongTrinhVersion = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createChuongTrinhVersion,
  updateChuongTrinhVersion,
  deleteChuongTrinhVersion,
};
