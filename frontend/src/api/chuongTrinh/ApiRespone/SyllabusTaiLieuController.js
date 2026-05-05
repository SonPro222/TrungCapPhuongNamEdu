import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/syllabus-tai-lieu';

export const getAllSyllabusTaiLieu = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getSyllabusTaiLieuById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllSyllabusTaiLieu,
  getSyllabusTaiLieuById,
};
