import apiClient from '../../apiConfig';

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
