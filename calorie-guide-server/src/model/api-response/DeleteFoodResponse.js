'use strict'

const Response = require('./Response')

/**
 * DeleteFoodResponse class used for creating REST responses for DELETE food/{id} API.
 */
class DeleteFoodResponse extends Response {

    /** @inheritDoc **/
    constructor(statusCode, body) {
        super(statusCode, body)
        Object.freeze(this)
    }

    /**
     * Creates a 200 response.
     *
     * @param {string} id - Food entry ID.
     * @returns {DeleteFoodResponse}
     */
    static okResponse(id) {
        const body = {
            message: `Successfully deleted ${id} food entry.`
        }
        return new DeleteFoodResponse(Response.StatusCode.OK, body)
    }

    /**
     * Creates a 404 response.
     *
     * @returns {DeleteFoodResponse}
     */
    static notFoundResponse(id) {
        const body = {
            message: `Food entry ${id} not found.`
        }
        return new DeleteFoodResponse(Response.StatusCode.NOT_FOUND, body)
    }
}

module.exports = DeleteFoodResponse