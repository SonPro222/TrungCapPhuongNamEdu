import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/nhom-tu-chon';

export const getAllNhomTuChon = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getNhomTuChonById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllNhomTuChon,
  getNhomTuChonById,
};
