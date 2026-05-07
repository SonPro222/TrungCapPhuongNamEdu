import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/syllabus-chuong-trinh';

export const getAllSyllabusChuongTrinh = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getSyllabusChuongTrinhById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllSyllabusChuongTrinh,
  getSyllabusChuongTrinhById,
};
