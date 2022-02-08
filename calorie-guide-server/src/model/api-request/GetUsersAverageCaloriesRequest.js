'use strict'

const AuthorizedRequest = require('./AuthorizedRequest')
const {isInteger, isPositiveInteger} = require('../../utils/utilFunctions')

/**
 * GetUsersAverageCaloriesRequest class used for creating and verifying GET report/user/calories/average API requests objects.
 */
class GetUsersAverageCaloriesRequest extends AuthorizedRequest {

    /** @inheritDoc **/
    constructor(event) {
        super(event)
        if (this.query) {
            this.limit = parseInt(this.query.limit, 10)
            this.exclusiveStartKey = this.query.exclusiveStartKey
            this.from = parseInt(this.query.from, 10)
            this.to = parseInt(this.query.to, 10)
        }
        Object.freeze(this)
    }

    /** @inheritDoc **/
    verifyFields() {
        if (this.limit && !isPositiveInteger(this.limit)) {
            throw new Error('Invalid request. Limit parameter needs to be a positive integer.')
        }

        if (this.from && !isInteger(this.from)) {
            throw new Error('Invalid request. From parameter needs to be a valid timestamp in milliseconds.')
        }

        if (this.to && !isInteger(this.to)) {
            throw new Error('Invalid request. From parameter needs to be a valid timestamp in milliseconds.')
        }
    }
}

module.exports = GetUsersAverageCaloriesRequest