'use strict'

const Response = require('./Response')

/**
 * GetFoodEntryCountResponse class used for creating REST responses for GET report/food/count API.
 */
class GetFoodEntryCountResponse extends Response {

    /** @inheritDoc **/
    constructor(statusCode, body) {
        super(statusCode, body)
        Object.freeze(this)
    }

    /**
     * Creates a 200 response.
     *
     * @param {number} count - Number of food entry items.
     * @returns {GetFoodEntryCountResponse}
     */
    static okResponse(count) {
        const body = {
            count
        }
        return new GetFoodEntryCountResponse(Response.StatusCode.OK, body)
    }
}

module.exports = GetFoodEntryCountResponse