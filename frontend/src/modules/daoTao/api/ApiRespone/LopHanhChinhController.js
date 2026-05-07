import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/dao-tao/lop-hanh-chinh';

export const getAllLopHanhChinh = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getLopHanhChinhById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllLopHanhChinh,
  getLopHanhChinhById,
};
