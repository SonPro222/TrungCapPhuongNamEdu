import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/lich-su-bai-tap-lop';

export const getAllLichSuBaiTapLop = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getLichSuBaiTapLopById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllLichSuBaiTapLop,
  getLichSuBaiTapLopById,
};
