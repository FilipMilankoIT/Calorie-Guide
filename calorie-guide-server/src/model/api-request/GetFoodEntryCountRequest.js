'use strict'

const AuthorizedRequest = require('./AuthorizedRequest')
const {isInteger} = require('../../utils/utilFunctions')

/**
 * GetFoodEntryCountRequest class used for creating and verifying GET report/food/count API requests objects.
 */
class GetFoodEntryCountRequest extends AuthorizedRequest {

    /** @inheritDoc **/
    constructor(event) {
        super(event)
        if (this.query) {
            this.from = parseInt(this.query.from, 10)
            this.to = parseInt(this.query.to, 10)
        }
        Object.freeze(this)
    }

    /** @inheritDoc **/
    verifyFields() {
        if (this.from && !isInteger(this.from)) {
            throw new Error('Invalid request. From parameter needs to be a valid timestamp in milliseconds.')
        }

        if (this.to && !isInteger(this.to)) {
            throw new Error('Invalid request. From parameter needs to be a valid timestamp in milliseconds.')
        }
    }
}

module.exports = GetFoodEntryCountRequest