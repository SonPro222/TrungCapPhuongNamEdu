import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/ket-qua-bai-tap-lop';

export const getAllKetQuaBaiTapLop = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getKetQuaBaiTapLopById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllKetQuaBaiTapLop,
  getKetQuaBaiTapLopById,
};
