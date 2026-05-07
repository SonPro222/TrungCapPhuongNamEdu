import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/dao-tao/nganh';

export const getAllNganh = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getNganhById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllNganh,
  getNganhById,
};
