import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/quy-doi-diem';

export const getAllQuyDoiDiem = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getQuyDoiDiemById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllQuyDoiDiem,
  getQuyDoiDiemById,
};
