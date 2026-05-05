import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/ca-hoc';

export const getAllCaHoc = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getCaHocById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllCaHoc,
  getCaHocById,
};
