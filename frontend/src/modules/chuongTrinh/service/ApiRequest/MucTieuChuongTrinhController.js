import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/muc-tieu-chuong-trinh';

export const createMucTieuChuongTrinh = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateMucTieuChuongTrinh = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteMucTieuChuongTrinh = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createMucTieuChuongTrinh,
  updateMucTieuChuongTrinh,
  deleteMucTieuChuongTrinh,
};
