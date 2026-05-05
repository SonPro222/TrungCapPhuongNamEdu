import apiClient from '../../apiConfig';

const ENDPOINT = '/api/dao-tao/khung-ky';

export const getAllKhungKy = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getKhungKyById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllKhungKy,
  getKhungKyById,
};
