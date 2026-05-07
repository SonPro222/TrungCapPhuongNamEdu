import apiClient from '../../apiConfig';

const ENDPOINT = '/api/hoc-phi/hoc-phi';

export const createHocPhi = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateHocPhi = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteHocPhi = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createHocPhi,
  updateHocPhi,
  deleteHocPhi,
};
