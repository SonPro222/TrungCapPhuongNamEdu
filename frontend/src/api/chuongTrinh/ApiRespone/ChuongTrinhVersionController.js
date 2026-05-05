import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/chuong-trinh-version';

export const getAllChuongTrinhVersion = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getChuongTrinhVersionById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllChuongTrinhVersion,
  getChuongTrinhVersionById,
};
