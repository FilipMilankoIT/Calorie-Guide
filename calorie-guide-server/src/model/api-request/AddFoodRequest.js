'use strict'

const AuthorizedRequest = require('./AuthorizedRequest')
const {isNonEmptyString, isInteger, isPositiveInteger} = require('../../utils/utilFunctions')

/**
 * AddFoodRequest class used for creating and verifying POST food API requests objects.
 */
class AddFoodRequest extends AuthorizedRequest {

    /** @inheritDoc **/
    constructor(event) {
        super(event)
        this.username = this.body.username ? this.body.username : this.senderUsername
        this.name = this.body.name
        this.timestamp = this.body.timestamp
        this.calories = this.body.calories
        Object.freeze(this)
    }

    /** @inheritDoc **/
    verifyFields() {
        if (this.username && !isNonEmptyString(this.username)) {
            throw new Error('Invalid food entry. Username must be a non empty string.')
        }

        if (!isNonEmptyString(this.name)) {
            throw new Error('Invalid food entry. Food name must be a non empty string.')
        }

        if (!isInteger(this.timestamp)) {
            throw new Error('Invalid food entry. Food timestamp need to be a timestamp in milliseconds.')
        }

        if (!isPositiveInteger(this.calories)) {
            throw new Error('Invalid food entry. Food calorie value needs to be a positive integer.')
        }
    }
}

module.exports = AddFoodRequest