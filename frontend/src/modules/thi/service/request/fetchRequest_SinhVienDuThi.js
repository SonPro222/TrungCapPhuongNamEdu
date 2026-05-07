import apiClient from '../../apiConfig';

const ENDPOINT = '/api/thi/sinh-vien-du-thi';

export const createSinhVienDuThi = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateSinhVienDuThi = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteSinhVienDuThi = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createSinhVienDuThi,
  updateSinhVienDuThi,
  deleteSinhVienDuThi,
};
