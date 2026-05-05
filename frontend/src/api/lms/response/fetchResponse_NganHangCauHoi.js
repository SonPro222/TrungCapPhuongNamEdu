import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/ngan-hang-cau-hoi';

export const getAllNganHangCauHoi = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getNganHangCauHoiById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllNganHangCauHoi,
  getNganHangCauHoiById,
};
