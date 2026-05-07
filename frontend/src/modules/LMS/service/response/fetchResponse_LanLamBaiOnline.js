import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/lan-lam-bai-online';

export const getAllLanLamBaiOnline = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getLanLamBaiOnlineById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllLanLamBaiOnline,
  getLanLamBaiOnlineById,
};
