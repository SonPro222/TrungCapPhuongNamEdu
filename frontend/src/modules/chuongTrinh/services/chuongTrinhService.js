import { chuongTrinhApi } from '@/modules/chuongTrinh/api/chuongTrinhApi.js';

const castValue = (value, type) => {
  if (type === 'checkbox') return Boolean(value);
  if (type === 'number') return value === '' || value === null || value === undefined ? null : Number(value);
  return value ?? '';
};

export const buildInitialForm = (resource, source = {}, routeQuery = {}) => {
  const form = {};

  for (const field of resource.fields || []) {
    const queryValue = routeQuery[field.key];
    const value = source[field.key] ?? queryValue ?? (field.type === 'checkbox' ? false : '');
    form[field.key] = castValue(value, field.type);
  }

  return form;
};

export const buildPayload = (resource, form) => {
  const payload = {};

  for (const field of resource.fields || []) {
    const value = castValue(form[field.key], field.type);

    if (value !== '' && value !== null && value !== undefined) {
      payload[field.key] = value;
    }
  }

  return payload;
};

export const fetchChuongTrinhList = (resource, filters) => chuongTrinhApi.getList(resource.endpoint, filters);
export const createChuongTrinhRecord = (resource, payload) => chuongTrinhApi.create(resource.endpoint, payload);
export const updateChuongTrinhRecord = (resource, id, payload) => chuongTrinhApi.update(resource.endpoint, id, payload);
export const deleteChuongTrinhRecord = (resource, id) => chuongTrinhApi.remove(resource.endpoint, id);
