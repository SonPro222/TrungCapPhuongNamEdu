import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/ngan-hang-cau-hoi';

export const createNganHangCauHoi = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateNganHangCauHoi = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteNganHangCauHoi = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createNganHangCauHoi,
  updateNganHangCauHoi,
  deleteNganHangCauHoi,
};
