'use strict'

const Response = require('./Response')

/**
 * GetFoodListResponse class used for creating REST responses for GET food API.
 */
class GetFoodListResponse extends Response {

    /** @inheritDoc **/
    constructor(statusCode, body) {
        super(statusCode, body)
        Object.freeze(this)
    }

    /**
     * Creates a 200 response.
     *
     * @param {Food[]} food - Array of Food objects.
     * @returns {GetFoodListResponse}
     */
    static okResponse(food) {
        const body = {
            items: food
        }
        return new GetFoodListResponse(Response.StatusCode.OK, body)
    }
}

module.exports = GetFoodListResponse