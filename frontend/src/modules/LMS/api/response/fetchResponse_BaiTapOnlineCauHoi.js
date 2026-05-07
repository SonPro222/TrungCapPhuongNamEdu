import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/bai-tap-online-cau-hoi';

export const getAllBaiTapOnlineCauHoi = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getBaiTapOnlineCauHoiById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllBaiTapOnlineCauHoi,
  getBaiTapOnlineCauHoiById,
};
