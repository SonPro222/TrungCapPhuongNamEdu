import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/yeu-cau-doi-lich';

export const getAllYeuCauDoiLich = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getYeuCauDoiLichById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllYeuCauDoiLich,
  getYeuCauDoiLichById,
};
