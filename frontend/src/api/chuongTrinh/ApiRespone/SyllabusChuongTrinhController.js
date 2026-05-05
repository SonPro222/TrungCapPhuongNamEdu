import apiClient from '../../apiConfig';

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
