import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/nhom-kien-thuc';

export const createNhomKienThuc = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateNhomKienThuc = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteNhomKienThuc = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createNhomKienThuc,
  updateNhomKienThuc,
  deleteNhomKienThuc,
};
