import apiClient from '../../apiConfig';

const ENDPOINT = '/api/dao-tao/khoa-dao-tao';

export const getAllKhoaDaoTao = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getKhoaDaoTaoById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllKhoaDaoTao,
  getKhoaDaoTaoById,
};
