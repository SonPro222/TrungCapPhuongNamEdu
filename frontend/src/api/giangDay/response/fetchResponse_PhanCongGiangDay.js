import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/phan-cong-giang-day';

export const getAllPhanCongGiangDay = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getPhanCongGiangDayById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllPhanCongGiangDay,
  getPhanCongGiangDayById,
};
