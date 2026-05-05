import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/yeu-cau-doi-lich';

export const createYeuCauDoiLich = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateYeuCauDoiLich = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteYeuCauDoiLich = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createYeuCauDoiLich,
  updateYeuCauDoiLich,
  deleteYeuCauDoiLich,
};
