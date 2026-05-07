import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/syllabus-chuong-trinh';

export const createSyllabusChuongTrinh = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateSyllabusChuongTrinh = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteSyllabusChuongTrinh = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createSyllabusChuongTrinh,
  updateSyllabusChuongTrinh,
  deleteSyllabusChuongTrinh,
};
