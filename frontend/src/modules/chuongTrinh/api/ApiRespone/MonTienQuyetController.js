import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/mon-tien-quyet';

export const getAllMonTienQuyet = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getMonTienQuyetById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllMonTienQuyet,
  getMonTienQuyetById,
};
