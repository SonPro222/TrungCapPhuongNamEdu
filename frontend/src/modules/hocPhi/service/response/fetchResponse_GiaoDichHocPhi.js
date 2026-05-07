import apiClient from '../../apiConfig';

const ENDPOINT = '/api/hoc-phi/giao-dich-hoc-phi';

export const getAllGiaoDichHocPhi = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getGiaoDichHocPhiById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export const getGiaoDichHocPhiByHocPhiId = (hocPhiId) => {
  return apiClient.get(`${ENDPOINT}/hoc-phi/${hocPhiId}`);
};

export default {
  getAllGiaoDichHocPhi,
  getGiaoDichHocPhiById,
  getGiaoDichHocPhiByHocPhiId,
};
