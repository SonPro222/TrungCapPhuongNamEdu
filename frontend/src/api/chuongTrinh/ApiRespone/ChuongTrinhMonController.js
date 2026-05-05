import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/chuong-trinh-mon';

export const getAllChuongTrinhMon = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getChuongTrinhMonById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllChuongTrinhMon,
  getChuongTrinhMonById,
};
