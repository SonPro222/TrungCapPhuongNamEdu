import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/muc-tieu-chuong-trinh';

export const getAllMucTieuChuongTrinh = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getMucTieuChuongTrinhById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllMucTieuChuongTrinh,
  getMucTieuChuongTrinhById,
};
