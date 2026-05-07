import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/diem/bai-kiem-tra';

export const getAllBaiKiemTra = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getBaiKiemTraById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllBaiKiemTra,
  getBaiKiemTraById,
};
