export const USER_ROLES = {
  ADMIN: 'ADMIN',
  ACADEMIC_OFFICE: 'ACADEMIC_OFFICE',
  LECTURER: 'LECTURER',
  STUDENT: 'STUDENT',
  ACCOUNTING: 'ACCOUNTING',
};

export const ROLE_LABELS = {
  [USER_ROLES.ADMIN]: 'Quản trị hệ thống',
  [USER_ROLES.ACADEMIC_OFFICE]: 'Phòng đào tạo',
  [USER_ROLES.LECTURER]: 'Giảng viên',
  [USER_ROLES.STUDENT]: 'Sinh viên',
  [USER_ROLES.ACCOUNTING]: 'Kế toán',
};

export default USER_ROLES;
