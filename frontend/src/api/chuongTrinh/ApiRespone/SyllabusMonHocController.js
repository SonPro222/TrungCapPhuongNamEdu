import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/syllabus-mon-hoc';

export const getAllSyllabusMonHoc = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getSyllabusMonHocById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllSyllabusMonHoc,
  getSyllabusMonHocById,
};
