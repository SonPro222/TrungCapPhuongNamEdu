import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/dap-an-cau-hoi';

export const getAllDapAnCauHoi = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getDapAnCauHoiById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllDapAnCauHoi,
  getDapAnCauHoiById,
};
