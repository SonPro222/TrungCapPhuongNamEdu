import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/nhat-ky-dang-nhap';

export const createNhatKyDangNhap = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateNhatKyDangNhap = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteNhatKyDangNhap = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createNhatKyDangNhap,
  updateNhatKyDangNhap,
  deleteNhatKyDangNhap,
};
