import apiClient from '../../apiConfig';

const ENDPOINT = '/api/dao-tao/khoa-dao-tao';

export const createKhoaDaoTao = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateKhoaDaoTao = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteKhoaDaoTao = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createKhoaDaoTao,
  updateKhoaDaoTao,
  deleteKhoaDaoTao,
};
