import apiClient from '../../apiConfig';

const ENDPOINT = '/api/dao-tao/loai-chuong-trinh';

export const createLoaiChuongTrinh = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateLoaiChuongTrinh = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteLoaiChuongTrinh = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createLoaiChuongTrinh,
  updateLoaiChuongTrinh,
  deleteLoaiChuongTrinh,
};
