'use strict'

const GetUserRequest = require('../../model/api-request/GetUserRequest')
const GetUserResponse = require('../../model/api-response/GetUserResponse')
const UserEntity = require('../../databse/UserEntity')
const Role = require('../../model/Role').Role
const ErrorCode = require('../../model/api-response/Response').ErrorCode
const {logError} = require('../../utils/errorUtils')

const ALLOWED_ROLES = new Set([Role.ADMIN])

/**
 * Get user Lambda function handler for responding to GET users/{username} API requests.
 *
 * @param {Object} event - Lambda event.
 * @returns {Promise<Response>} - Returns REST API response.
 */
module.exports.handler = async (event) => {
    console.log("Received event:\n", JSON.stringify(event))

    let request

    try {
        request = new GetUserRequest(event)
        console.log("Received request:\n", JSON.stringify(request))

        request.verify()
    } catch (error) {
        logError(error)
        return GetUserResponse.badRequestResponse(ErrorCode.INVALID_REQUEST, error.message)
    }

    if (!await hasPermission(request)) {
        return GetUserResponse.forbiddenResponse()
    }

    try {
        const userEntity = new UserEntity(process.env.userTable)
        const user = await userEntity.get(request.username)

        if (!user) {
            console.error(`Username ${request.username} not found`)
            return GetUserResponse.userNotFound(request.username)
        }

        console.log('Successfully retrieved user:\n', JSON.stringify(user))

        return GetUserResponse.okResponse(user)
    } catch (error) {
        logError(error)
        return GetUserResponse.internalErrorResponse()
    }
}

/**
 *  Checks if the requester has permission for this request.
 *
 * @param {GetUserRequest} request - GetUserRequest object.
 * @returns {Promise<boolean>} - Returns Promise with true if the requester has permission and false otherwise.
 */
async function hasPermission(request) {
    if (request.username === request.senderUsername)  return true
    const userEntity = new UserEntity(process.env.userTable)
    const role = await userEntity.getRole(request.senderUsername)
    return ALLOWED_ROLES.has(role)
}