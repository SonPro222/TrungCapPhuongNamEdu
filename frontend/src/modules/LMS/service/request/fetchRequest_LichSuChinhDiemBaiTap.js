import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/lich-su-chinh-diem-bai-tap';

export const createLichSuChinhDiemBaiTap = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateLichSuChinhDiemBaiTap = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteLichSuChinhDiemBaiTap = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createLichSuChinhDiemBaiTap,
  updateLichSuChinhDiemBaiTap,
  deleteLichSuChinhDiemBaiTap,
};
