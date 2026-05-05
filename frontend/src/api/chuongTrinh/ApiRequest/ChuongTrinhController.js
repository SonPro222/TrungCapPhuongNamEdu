import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/chuong-trinh';

export const createChuongTrinh = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateChuongTrinh = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteChuongTrinh = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createChuongTrinh,
  updateChuongTrinh,
  deleteChuongTrinh,
};
