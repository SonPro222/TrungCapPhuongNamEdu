import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/sinh-vien/mien-tru-bao-luu';

export const getAllMienTruBaoLuu = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getMienTruBaoLuuById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllMienTruBaoLuu,
  getMienTruBaoLuuById,
};
