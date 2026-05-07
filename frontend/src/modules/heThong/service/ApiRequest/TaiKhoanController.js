import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/tai-khoan';

export const createTaiKhoan = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateTaiKhoan = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteTaiKhoan = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createTaiKhoan,
  updateTaiKhoan,
  deleteTaiKhoan,
};
