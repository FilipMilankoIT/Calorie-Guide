'use strict'

const Response = require('./Response')

/**
 * AddFoodResponse class used for creating REST responses for POST food API.
 */
class AddFoodResponse extends Response {

    /** @inheritDoc **/
    constructor(statusCode, body) {
        super(statusCode, body)
        Object.freeze(this)
    }

    /**
     * Creates a 404 response.
     *
     * @param {username} username - User's username.
     * @returns {AddFoodResponse}
     */
    static userNotFound(username) {
        const body = {
            code: Response.ErrorCode.USERNAME_NOT_FOUND,
            message: `User ${username} not found.`
        }
        return new AddFoodResponse(Response.StatusCode.NOT_FOUND, body)
    }

    /**
     * Creates a 201 response.
     *
     * @param {Food} Food - Food objet.
     * @returns {AddFoodResponse}
     */
    static createdResponse(Food) {
        return new AddFoodResponse(Response.StatusCode.CREATED, Food)
    }
}

module.exports = AddFoodResponse