import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/diem/cau-hinh-danh-gia';

export const getAllCauHinhDanhGia = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getCauHinhDanhGiaById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllCauHinhDanhGia,
  getCauHinhDanhGiaById,
};
