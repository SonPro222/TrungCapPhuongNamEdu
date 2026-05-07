import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/diem/ket-qua-mon-hoc';

export const getAllKetQuaMonHoc = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getKetQuaMonHocById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllKetQuaMonHoc,
  getKetQuaMonHocById,
};
