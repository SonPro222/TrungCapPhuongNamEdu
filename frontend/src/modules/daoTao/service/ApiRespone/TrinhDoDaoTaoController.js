import apiClient from '../../apiConfig';

const ENDPOINT = '/api/dao-tao/trinh-do-dao-tao';

export const getAllTrinhDoDaoTao = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getTrinhDoDaoTaoById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllTrinhDoDaoTao,
  getTrinhDoDaoTaoById,
};
