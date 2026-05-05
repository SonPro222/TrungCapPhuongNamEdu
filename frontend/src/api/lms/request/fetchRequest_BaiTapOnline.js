import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/bai-tap-online';

export const createBaiTapOnline = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateBaiTapOnline = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteBaiTapOnline = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createBaiTapOnline,
  updateBaiTapOnline,
  deleteBaiTapOnline,
};
