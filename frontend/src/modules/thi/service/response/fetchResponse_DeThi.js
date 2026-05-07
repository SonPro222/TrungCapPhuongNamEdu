import apiClient from '../../apiConfig';

const ENDPOINT = '/api/thi/de-thi';

export const getAllDeThi = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getDeThiById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllDeThi,
  getDeThiById,
};
