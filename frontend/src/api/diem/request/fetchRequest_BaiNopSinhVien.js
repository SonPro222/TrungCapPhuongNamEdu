import apiClient from '../../apiConfig';

const ENDPOINT = '/api/diem/bai-nop-sinh-vien';

export const createBaiNopSinhVien = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateBaiNopSinhVien = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteBaiNopSinhVien = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createBaiNopSinhVien,
  updateBaiNopSinhVien,
  deleteBaiNopSinhVien,
};
