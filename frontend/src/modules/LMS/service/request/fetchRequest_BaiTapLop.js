import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/bai-tap-lop';

export const createBaiTapLop = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateBaiTapLop = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteBaiTapLop = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createBaiTapLop,
  updateBaiTapLop,
  deleteBaiTapLop,
};
