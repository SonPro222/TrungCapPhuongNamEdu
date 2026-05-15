import apiClient from '@/core/api/apiClient';
import { unwrapListResponse, unwrapResponse } from '@/core/api/axiosClient';

const normalizeParams = (params = {}) => {
  const cleaned = {};

  Object.entries(params).forEach(([key, value]) => {
    if (value !== '' && value !== null && value !== undefined) {
      cleaned[key] = value;
    }
  });

  return cleaned;
};

export const chuongTrinhApi = {
  async getList(endpoint, params = {}) {
    const response = await apiClient.get(endpoint, {
      params: normalizeParams(params),
    });

    return unwrapListResponse(response);
  },

  async getDetail(endpoint, id) {
    const response = await apiClient.get(`${endpoint}/${id}`);
    return unwrapResponse(response);
  },

  async create(endpoint, payload) {
    const response = await apiClient.post(endpoint, payload);
    return unwrapResponse(response);
  },

  async update(endpoint, id, payload) {
    const response = await apiClient.put(`${endpoint}/${id}`, payload);
    return unwrapResponse(response);
  },

  async remove(endpoint, id) {
    const response = await apiClient.delete(`${endpoint}/${id}`);
    return unwrapResponse(response);
  },
};

export const getRecordId = (record, idKey = 'id') => record?.[idKey] ?? record?.id ?? record?.ID ?? record?._id;
