import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/lop-hoc-phan';

export const getAllLopHocPhan = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getLopHocPhanById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllLopHocPhan,
  getLopHocPhanById,
};
