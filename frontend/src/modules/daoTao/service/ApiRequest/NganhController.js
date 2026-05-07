import apiClient from '../../apiConfig';

const ENDPOINT = '/api/dao-tao/nganh';

export const createNganh = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateNganh = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteNganh = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createNganh,
  updateNganh,
  deleteNganh,
};
