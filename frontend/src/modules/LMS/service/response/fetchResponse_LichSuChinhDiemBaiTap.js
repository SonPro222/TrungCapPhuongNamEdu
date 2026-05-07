import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/lich-su-chinh-diem-bai-tap';

export const getAllLichSuChinhDiemBaiTap = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getLichSuChinhDiemBaiTapById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllLichSuChinhDiemBaiTap,
  getLichSuChinhDiemBaiTapById,
};
