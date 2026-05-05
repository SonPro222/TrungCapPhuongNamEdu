import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/syllabus-chuong-bai';

export const createSyllabusChuongBai = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateSyllabusChuongBai = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteSyllabusChuongBai = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createSyllabusChuongBai,
  updateSyllabusChuongBai,
  deleteSyllabusChuongBai,
};
