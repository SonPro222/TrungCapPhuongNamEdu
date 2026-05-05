import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/lich-su-bai-tap-lop';

export const createLichSuBaiTapLop = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateLichSuBaiTapLop = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteLichSuBaiTapLop = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createLichSuBaiTapLop,
  updateLichSuBaiTapLop,
  deleteLichSuBaiTapLop,
};
