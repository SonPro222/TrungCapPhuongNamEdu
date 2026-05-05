import apiClient from '../../apiConfig';

const ENDPOINT = '/api/diem/bai-kiem-tra';

export const createBaiKiemTra = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateBaiKiemTra = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteBaiKiemTra = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createBaiKiemTra,
  updateBaiKiemTra,
  deleteBaiKiemTra,
};
