import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/dot-giao-bai-online';

export const getAllDotGiaoBaiOnline = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getDotGiaoBaiOnlineById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllDotGiaoBaiOnline,
  getDotGiaoBaiOnlineById,
};
