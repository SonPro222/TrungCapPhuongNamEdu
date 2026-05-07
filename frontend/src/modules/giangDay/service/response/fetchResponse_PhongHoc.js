import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/phong-hoc';

export const getAllPhongHoc = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getPhongHocById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllPhongHoc,
  getPhongHocById,
};
