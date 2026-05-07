import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/mon-hoc';

export const getAllMonHoc = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getMonHocById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllMonHoc,
  getMonHocById,
};
