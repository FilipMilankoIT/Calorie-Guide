'use strict'

const AuthorizedRequest = require('./AuthorizedRequest')
const {isNonEmptyString} = require("../../utils/utilFunctions");

/**
 * GetUserRequest class used for creating and verifying GET users/{username} API requests objects.
 */
class GetUserRequest extends AuthorizedRequest {

    /** @inheritDoc **/
    constructor(event) {
        super(event)
        if (this.path) {
            this.username = this.path.username
        }
        Object.freeze(this)
    }

    /** @inheritDoc **/
    verifyFields() {
        if (!isNonEmptyString(this.username)) {
            throw new Error('Invalid request. Missing username path parameter.')
        }
    }
}

module.exports = GetUserRequest