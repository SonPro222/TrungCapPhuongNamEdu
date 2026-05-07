import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/nhan-vien';

export const createNhanVien = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateNhanVien = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteNhanVien = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createNhanVien,
  updateNhanVien,
  deleteNhanVien,
};
