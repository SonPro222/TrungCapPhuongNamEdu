import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/mon-tu-chon';

export const getAllMonTuChon = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getMonTuChonById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllMonTuChon,
  getMonTuChonById,
};
