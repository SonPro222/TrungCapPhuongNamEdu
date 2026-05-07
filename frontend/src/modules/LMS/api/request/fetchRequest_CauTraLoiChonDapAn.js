import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/cau-tra-loi-chon-dap-an';

export const createCauTraLoiChonDapAn = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateCauTraLoiChonDapAn = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteCauTraLoiChonDapAn = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createCauTraLoiChonDapAn,
  updateCauTraLoiChonDapAn,
  deleteCauTraLoiChonDapAn,
};
