import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/phong-hoc';

export const createPhongHoc = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updatePhongHoc = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deletePhongHoc = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createPhongHoc,
  updatePhongHoc,
  deletePhongHoc,
};
