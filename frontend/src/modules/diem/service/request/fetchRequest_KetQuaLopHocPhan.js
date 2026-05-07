import apiClient from '../../apiConfig';

const ENDPOINT = '/api/diem/ket-qua-lop-hoc-phan';

export const createKetQuaLopHocPhan = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateKetQuaLopHocPhan = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteKetQuaLopHocPhan = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createKetQuaLopHocPhan,
  updateKetQuaLopHocPhan,
  deleteKetQuaLopHocPhan,
};
