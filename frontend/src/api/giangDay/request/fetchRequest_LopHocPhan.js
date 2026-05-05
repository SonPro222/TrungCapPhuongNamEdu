import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/lop-hoc-phan';

export const createLopHocPhan = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateLopHocPhan = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteLopHocPhan = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createLopHocPhan,
  updateLopHocPhan,
  deleteLopHocPhan,
};
