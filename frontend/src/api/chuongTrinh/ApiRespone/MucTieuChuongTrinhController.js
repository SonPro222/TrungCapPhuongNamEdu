import apiClient from '../../apiConfig';

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
