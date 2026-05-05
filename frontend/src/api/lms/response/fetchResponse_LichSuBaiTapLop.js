import apiClient from '../../apiConfig';

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
