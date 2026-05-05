import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/phan-cong-giang-day';

export const createPhanCongGiangDay = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updatePhanCongGiangDay = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deletePhanCongGiangDay = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createPhanCongGiangDay,
  updatePhanCongGiangDay,
  deletePhanCongGiangDay,
};
