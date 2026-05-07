import { toNumberOrNull } from '@/api/apiResponse.js';

export function normalizeValue(value, fieldType) {
  if (fieldType === 'checkbox') return Boolean(value);
  if (fieldType === 'number') return toNumberOrNull(value);
  if (fieldType === 'select') return value === '' || value === undefined || value === null ? null : Number(value);
  return value === undefined ? null : value;
}

export function buildPayload(form = {}, fields = []) {
  return fields.reduce((payload, field) => {
    payload[field.key] = normalizeValue(form[field.key], field.type);
    return payload;
  }, {});
}

export function initialForm(fields = [], defaults = {}) {
  return fields.reduce((form, field) => {
    if (defaults[field.key] !== undefined) form[field.key] = defaults[field.key];
    else form[field.key] = field.type === 'checkbox' ? false : '';
    return form;
  }, {});
}
