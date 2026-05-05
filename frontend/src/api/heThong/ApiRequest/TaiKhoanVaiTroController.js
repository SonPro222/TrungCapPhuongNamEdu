import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/tai-khoan-vai-tro';

export const createTaiKhoanVaiTro = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateTaiKhoanVaiTro = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteTaiKhoanVaiTro = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createTaiKhoanVaiTro,
  updateTaiKhoanVaiTro,
  deleteTaiKhoanVaiTro,
};
