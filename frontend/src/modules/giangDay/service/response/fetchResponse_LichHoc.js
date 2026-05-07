import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/lich-hoc';

export const getAllLichHoc = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getLichHocById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllLichHoc,
  getLichHocById,
};
