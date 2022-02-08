'use strict'

const Response = require('./Response')

/**
 * GetUsersAverageCaloriesResponse class used for creating REST responses for GET report/user/calories/average API.
 */
class GetUsersAverageCaloriesResponse extends Response {

    /** @inheritDoc **/
    constructor(statusCode, body) {
        super(statusCode, body)
        Object.freeze(this)
    }

    /**
     * Creates a 200 response.
     *
     * @param {AverageUserCalories[]} items - Array of AverageUserCalories objects.
     * @param {string} [lastEvaluatedKey]
     * @returns {GetUsersAverageCaloriesResponse}
     */
    static okResponse(items, lastEvaluatedKey) {
        const body = {
            items,
            lastEvaluatedKey
        }
        return new GetUsersAverageCaloriesResponse(Response.StatusCode.OK, body)
    }

    /**
     * Data class for holding users average calorie count
     */
    static AverageUserCalories = class AverageUserCalories {

        /**
         * Constructor for AverageUserCalories class.
         *
         * @param {string} username - User's username.
         * @param {number} count - Average calorie count.
         */
        constructor(username, count) {
            this.username = username
            this.count = count
            Object.freeze(this)
        }
    }
}

module.exports = GetUsersAverageCaloriesResponse