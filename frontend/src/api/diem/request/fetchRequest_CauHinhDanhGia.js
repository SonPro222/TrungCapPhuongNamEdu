import apiClient from '../../apiConfig';

const ENDPOINT = '/api/diem/cau-hinh-danh-gia';

export const createCauHinhDanhGia = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateCauHinhDanhGia = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteCauHinhDanhGia = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createCauHinhDanhGia,
  updateCauHinhDanhGia,
  deleteCauHinhDanhGia,
};
