'use strict'

const Response = require('./Response')

/**
 * UpdateFoodResponse class used for creating REST responses for PATCH food/{id} API.
 */
class UpdateFoodResponse extends Response {

    /** @inheritDoc **/
    constructor(statusCode, body) {
        super(statusCode, body)
        Object.freeze(this)
    }

    /**
     * Creates a 200 response.
     *
     * @param {Food} food - Food objet.
     * @returns {UpdateFoodResponse}
     */
    static okResponse(food) {
        return new UpdateFoodResponse(Response.StatusCode.OK, food)
    }

    /**
     * Creates a 404 response.
     *
     * @param {string} id - Food entry ID.
     * @returns {UpdateFoodResponse}
     */
    static notFoundResponse(id) {
        const body = {
            code: Response.ErrorCode.ITEM_NOT_FOUND,
            message: `Food entry ${id} not found.`
        }
        return new UpdateFoodResponse(Response.StatusCode.NOT_FOUND, body)
    }
}

module.exports = UpdateFoodResponse