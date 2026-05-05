import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/dap-an-cau-hoi';

export const createDapAnCauHoi = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateDapAnCauHoi = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteDapAnCauHoi = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createDapAnCauHoi,
  updateDapAnCauHoi,
  deleteDapAnCauHoi,
};
