import apiClient from '../../apiConfig';

const ENDPOINT = '/api/thi/bai-thi-sinh-vien';

export const createBaiThiSinhVien = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateBaiThiSinhVien = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteBaiThiSinhVien = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createBaiThiSinhVien,
  updateBaiThiSinhVien,
  deleteBaiThiSinhVien,
};
