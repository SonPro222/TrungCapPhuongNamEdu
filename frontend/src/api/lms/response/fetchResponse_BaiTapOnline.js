import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/bai-tap-online';

export const getAllBaiTapOnline = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getBaiTapOnlineById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllBaiTapOnline,
  getBaiTapOnlineById,
};
