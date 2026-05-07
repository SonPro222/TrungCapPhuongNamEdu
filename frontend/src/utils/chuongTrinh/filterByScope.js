import { matchKeyword } from '@/api/apiResponse.js';

export function filterByParent(list = [], relation, parentRow) {
  if (!relation || !parentRow?.id) return list;
  return list.filter((item) => Number(item?.[relation.parentField]) === Number(parentRow.id));
}

export function filterByKeyword(list = [], keyword = '', fields = []) {
  return list.filter((item) => matchKeyword(item, keyword, fields));
}
