import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/quy-doi-diem';

export const createQuyDoiDiem = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateQuyDoiDiem = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteQuyDoiDiem = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createQuyDoiDiem,
  updateQuyDoiDiem,
  deleteQuyDoiDiem,
};
