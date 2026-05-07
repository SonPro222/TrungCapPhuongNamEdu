import apiClient from '../../apiConfig';

const ENDPOINT = '/api/diem/ket-qua-lop-hoc-phan';

export const getAllKetQuaLopHocPhan = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getKetQuaLopHocPhanById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllKetQuaLopHocPhan,
  getKetQuaLopHocPhanById,
};
