import apiClient from '../../apiConfig';

const ENDPOINT = '/api/diem/ket-qua-mon-hoc';

export const createKetQuaMonHoc = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateKetQuaMonHoc = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteKetQuaMonHoc = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createKetQuaMonHoc,
  updateKetQuaMonHoc,
  deleteKetQuaMonHoc,
};
