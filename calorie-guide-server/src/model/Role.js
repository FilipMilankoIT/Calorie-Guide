'use strict'

/**
 * Enum object for user role.
 *
 * @type {Readonly<{USER: string, ADMIN: string}>}
 * @enum {string}
 */
const Role = Object.freeze({
    USER: "user",
    ADMIN: "admin"
})

/**
 * Get the rank of a role.
 *
 * @param {Role} role
 * @returns {number}
 */
function getRoleRank(role) {
    switch (role) {
        case Role.USER:
            return 1
        case Role.ADMIN:
            return 2
        default:
            return 0
    }
}

module.exports = {
    Role,
    getRoleRank
}