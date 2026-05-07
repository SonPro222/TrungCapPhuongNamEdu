import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/lich-su-day-diem-lms';

export const getAllLichSuDayDiemLms = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getLichSuDayDiemLmsById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllLichSuDayDiemLms,
  getLichSuDayDiemLmsById,
};
