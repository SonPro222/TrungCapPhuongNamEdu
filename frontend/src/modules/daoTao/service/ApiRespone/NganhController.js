import apiClient from '../../apiConfig';

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
