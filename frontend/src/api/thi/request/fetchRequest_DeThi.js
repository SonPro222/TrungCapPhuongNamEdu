import apiClient from '../../apiConfig';

const ENDPOINT = '/api/thi/de-thi';

export const createDeThi = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateDeThi = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteDeThi = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createDeThi,
  updateDeThi,
  deleteDeThi,
};
