'use strict'

const AuthorizedRequest = require('./AuthorizedRequest')
const {isNonEmptyString, isInteger, isPositiveInteger} = require('../../utils/utilFunctions')

/**
 * UpdateFoodRequest class used for creating and verifying PATCH food/{id} API requests objects.
 */
class UpdateFoodRequest extends AuthorizedRequest {

    /** @inheritDoc **/
    constructor(event) {
        super(event)
        if (this.path) {
            this.id = this.path.id
        }
        this.name = this.body.name
        this.timestamp = this.body.timestamp
        this.calories = this.body.calories
        Object.freeze(this)
    }

    /** @inheritDoc **/
    verifyFields() {
        if (!isNonEmptyString(this.id)) {
            throw new Error('Invalid request. Missing id path parameter.')
        }

        if (this.name === undefined && !this.timestamp && this.calories) {
            throw new Error('Invalid food entry update parameters.')
        }

        if (this.name && !isNonEmptyString(this.name)) {
            throw new Error('Invalid food. Food name must be a non empty string.')
        }

        if (this.timestamp && !isInteger(this.timestamp)) {
            throw new Error('Invalid food. Food timestamp need to be a timestamp in milliseconds.')
        }

        if (this.calories && !isPositiveInteger(this.calories)) {
            throw new Error('Invalid food. Food calorie value needs to be a positive integer.')
        }
    }
}

module.exports = UpdateFoodRequest