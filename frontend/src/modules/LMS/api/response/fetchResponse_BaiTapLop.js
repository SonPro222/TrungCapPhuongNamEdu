import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/bai-tap-lop';

export const getAllBaiTapLop = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getBaiTapLopById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllBaiTapLop,
  getBaiTapLopById,
};
