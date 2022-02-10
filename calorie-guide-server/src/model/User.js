'use strict'

const {hashPassword} = require("../utils/passwordUtils")

/**
 * User data class.
 */
class User {

    static DEFAULT_DAILY_CALORIE_LIMIT = 2100

    /**
     * Constructor for User class.
     *
     * @param {string} username - Username.
     * @param {string} password - User's plain text password.
     * @param {Role} role - User role.
     * @param {string} [firstName] - User's first name.
     * @param {string} [lastName] - User's last name.
     * @param {Gender} [gender] - User's gender.
     * @param {number} [birthday] - User's birthday.
     * @param {number} [dailyCalorieLimit] - User's daily calorie limit.
     */
    constructor(username, password, role, firstName, lastName, gender,
                birthday, dailyCalorieLimit) {
        this.username = username
        this.hashPassword = password ? hashPassword(password) : undefined
        this.role = role
        this.firstName = firstName
        this.lastName = lastName
        this.gender = gender
        this.birthday = birthday
        this.birthday = birthday
        this.dailyCalorieLimit = dailyCalorieLimit
        Object.freeze(this)
    }
}

module.exports = User