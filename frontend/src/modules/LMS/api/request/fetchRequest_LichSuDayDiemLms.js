import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/lich-su-day-diem-lms';

export const createLichSuDayDiemLms = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateLichSuDayDiemLms = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteLichSuDayDiemLms = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createLichSuDayDiemLms,
  updateLichSuDayDiemLms,
  deleteLichSuDayDiemLms,
};
