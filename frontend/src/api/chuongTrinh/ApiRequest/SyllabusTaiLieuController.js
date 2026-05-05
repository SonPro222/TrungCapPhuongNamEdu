import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/syllabus-tai-lieu';

export const createSyllabusTaiLieu = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateSyllabusTaiLieu = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteSyllabusTaiLieu = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createSyllabusTaiLieu,
  updateSyllabusTaiLieu,
  deleteSyllabusTaiLieu,
};
