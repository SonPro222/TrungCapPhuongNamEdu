import apiClient from '../../apiConfig';

const ENDPOINT = '/api/dao-tao/trinh-do-dao-tao';

export const createTrinhDoDaoTao = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateTrinhDoDaoTao = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteTrinhDoDaoTao = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createTrinhDoDaoTao,
  updateTrinhDoDaoTao,
  deleteTrinhDoDaoTao,
};
