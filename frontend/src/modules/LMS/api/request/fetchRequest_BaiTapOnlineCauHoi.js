import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/bai-tap-online-cau-hoi';

export const createBaiTapOnlineCauHoi = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateBaiTapOnlineCauHoi = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteBaiTapOnlineCauHoi = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createBaiTapOnlineCauHoi,
  updateBaiTapOnlineCauHoi,
  deleteBaiTapOnlineCauHoi,
};
