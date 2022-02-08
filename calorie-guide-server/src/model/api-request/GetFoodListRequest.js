'use strict'

const AuthorizedRequest = require('./AuthorizedRequest')
const {isNonEmptyString, isInteger} = require('../../utils/utilFunctions')

/**
 * GetFoodListRequest class used for creating and verifying GET food API requests objects.
 */
class GetFoodListRequest extends AuthorizedRequest {

    /** @inheritDoc **/
    constructor(event) {
        super(event)
        if (this.query) {
            this.username = this.query.username
            this.from = parseInt(this.query.from, 10)
            this.to = parseInt(this.query.to, 10)
        }
        Object.freeze(this)
    }

    /** @inheritDoc **/
    verifyFields() {
        if (this.username && !isNonEmptyString(this.username)) {
            throw new Error('Invalid username. Username must be a non empty string.')
        }

        if (this.from && !isInteger(this.from)) {
            throw new Error('Invalid request. From parameter needs to be a valid timestamp in milliseconds.')
        }

        if (this.to && !isInteger(this.to)) {
            throw new Error('Invalid request. From parameter needs to be a valid timestamp in milliseconds.')
        }
    }
}

module.exports = GetFoodListRequest