'use strict'

const AuthorizedRequest = require('./AuthorizedRequest')
const {isNonEmptyString} = require('../../utils/utilFunctions')

/**
 * DeleteFoodRequest class used for creating and verifying DELETE food/{id} API requests objects.
 */
class DeleteFoodRequest extends AuthorizedRequest {

    /** @inheritDoc **/
    constructor(event) {
        super(event)
        if (this.path) {
            this.id = this.path.id
        }
        Object.freeze(this)
    }

    /** @inheritDoc **/
    verifyFields() {
        if (!isNonEmptyString(this.id)) {
            throw new Error('Invalid request. Missing id path parameter.')
        }
    }
}

module.exports = DeleteFoodRequest