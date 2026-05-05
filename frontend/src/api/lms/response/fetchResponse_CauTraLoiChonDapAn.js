import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/cau-tra-loi-chon-dap-an';

export const getAllCauTraLoiChonDapAn = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getCauTraLoiChonDapAnById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllCauTraLoiChonDapAn,
  getCauTraLoiChonDapAnById,
};
