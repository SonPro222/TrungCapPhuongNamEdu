import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/mon-hoc';

export const createMonHoc = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateMonHoc = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteMonHoc = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createMonHoc,
  updateMonHoc,
  deleteMonHoc,
};
