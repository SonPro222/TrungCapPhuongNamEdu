import apiClient from '../../apiConfig';

const ENDPOINT = '/api/hoc-phi/hoc-phi';

export const getAllHocPhi = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getHocPhiById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export const getHocPhiBySinhVienChuongTrinhId = (sinhVienChuongTrinhId) => {
  return apiClient.get(`${ENDPOINT}/sinh-vien-chuong-trinh/${sinhVienChuongTrinhId}`);
};

export const getHocPhiByTrangThai = (trangThai) => {
  return apiClient.get(`${ENDPOINT}/trang-thai/${trangThai}`);
};

export default {
  getAllHocPhi,
  getHocPhiById,
  getHocPhiBySinhVienChuongTrinhId,
  getHocPhiByTrangThai,
};
