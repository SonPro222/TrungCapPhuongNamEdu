import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/cau-tra-loi-sinh-vien';

export const createCauTraLoiSinhVien = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateCauTraLoiSinhVien = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteCauTraLoiSinhVien = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createCauTraLoiSinhVien,
  updateCauTraLoiSinhVien,
  deleteCauTraLoiSinhVien,
};
