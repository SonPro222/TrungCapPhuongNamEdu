import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/dao-tao/lop-hanh-chinh';

export const createLopHanhChinh = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateLopHanhChinh = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteLopHanhChinh = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createLopHanhChinh,
  updateLopHanhChinh,
  deleteLopHanhChinh,
};
