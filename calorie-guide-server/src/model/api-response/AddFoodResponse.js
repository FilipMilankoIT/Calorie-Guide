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