import apiClient from '../../apiConfig';

const ENDPOINT = '/api/hoc-phi/giao-dich-hoc-phi';

export const createGiaoDichHocPhi = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateGiaoDichHocPhi = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteGiaoDichHocPhi = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createGiaoDichHocPhi,
  updateGiaoDichHocPhi,
  deleteGiaoDichHocPhi,
};
