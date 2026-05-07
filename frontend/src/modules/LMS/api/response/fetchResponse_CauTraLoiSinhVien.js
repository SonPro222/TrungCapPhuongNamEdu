import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/cau-tra-loi-sinh-vien';

export const getAllCauTraLoiSinhVien = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getCauTraLoiSinhVienById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllCauTraLoiSinhVien,
  getCauTraLoiSinhVienById,
};
