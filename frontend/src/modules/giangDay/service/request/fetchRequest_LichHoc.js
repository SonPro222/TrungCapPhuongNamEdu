import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/lich-hoc';

export const createLichHoc = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateLichHoc = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteLichHoc = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createLichHoc,
  updateLichHoc,
  deleteLichHoc,
};
