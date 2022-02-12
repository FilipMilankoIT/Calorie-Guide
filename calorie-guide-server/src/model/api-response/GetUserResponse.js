'use strict'

const Response = require('./Response')

/**
 * GetUserResponse class used for creating REST responses for GET user API.
 */
class GetUserResponse extends Response {

    /** @inheritDoc **/
    constructor(statusCode, body) {
        super(statusCode, body)
        Object.freeze(this)
    }

    /**
     * Creates a 200 response.
     *
     * @param {User} user - User object.
     * @returns {GetUserResponse}
     */
    static okResponse(user) {
        delete user.hashPassword
        return new GetUserResponse(Response.StatusCode.OK, user)
    }

    /**
     * Creates a 404 response.
     *
     * @param {username} username - User's username.
     * @returns {GetUserResponse}
     */
    static userNotFound(username) {
        const body = {
            code: Response.ErrorCode.USERNAME_NOT_FOUND,
            message: `User ${username} not found.`
        }
        return new GetUserResponse(Response.StatusCode.NOT_FOUND, body)
    }
}

module.exports = GetUserResponse