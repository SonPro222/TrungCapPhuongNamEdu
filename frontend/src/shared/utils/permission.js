export function hasPermission(user, permission) {
    const permissions = user?.permissions || []
    return permissions.includes(permission)
}

export function hasAnyPermission(user, permissions = []) {
    return permissions.some((permission) => hasPermission(user, permission))
}