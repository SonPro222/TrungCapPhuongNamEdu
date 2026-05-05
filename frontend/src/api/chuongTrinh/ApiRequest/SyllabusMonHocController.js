import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/syllabus-mon-hoc';

export const createSyllabusMonHoc = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateSyllabusMonHoc = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteSyllabusMonHoc = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createSyllabusMonHoc,
  updateSyllabusMonHoc,
  deleteSyllabusMonHoc,
};
