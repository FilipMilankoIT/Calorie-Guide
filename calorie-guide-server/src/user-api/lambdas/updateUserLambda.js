'use strict'

const UpdateUserRequest = require('../../model/api-request/UpdateUserRequest')
const UpdateUserResponse = require('../../model/api-response/UpdateUserResponse')
const UserEntity = require('../../databse/UserEntity')
const User = require('../../model/User')
const Role = require('../../model/Role').Role
const ErrorCode = require('../../model/api-response/Response').ErrorCode
const {getRoleRank} = require('../../model/Role')
const {logError} = require('../../utils/errorUtils')

const ALLOWED_ROLES = new Set([Role.ADMIN])

/**
 * Update user Lambda function handler for responding to PATCH users/{username} API requests.
 *
 * @param {Object} event - Lambda event.
 * @returns {Promise<Response>} - Returns REST API response.
 */
module.exports.handler = async (event) => {
    console.log("Received event:\n", JSON.stringify(event))

    let request

    try {
        request = new UpdateUserRequest(event)
        console.log("Received request:\n", JSON.stringify(request))

        request.verify()
    } catch (error) {
        logError(error)
        return UpdateUserResponse.badRequestResponse(ErrorCode.INVALID_REQUEST, error.message)
    }

    try {
        const userEntity = new UserEntity(process.env.userTable)

        const role = await userEntity.getRole(request.senderUsername)

        if (!await hasPermission(request, role)) {
            return UpdateUserResponse.forbiddenResponse()
        }

        const user = await userEntity.get(request.username)

        if (!user) {
            console.error(`Username ${request.username} not found`)
            return UpdateUserResponse.notFoundResponse(request.username);
        }

        if (getRoleRank(role) < getRoleRank(user.role)) {
            console.error('Can\'t update user with higher role')
            return UpdateUserResponse.updateHigherRoleUserResponse()
        }

        if (request.role && getRoleRank(role) < getRoleRank(request.role)) {
            console.error(`Don\'t have permission to update to ${request.role} role.`)
            return UpdateUserResponse.roleUpdateForbiddenResponse(request.role)
        }

        const updatedUser = new User(
            request.username,
            request.password,
            request.role,
            request.firstName,
            request.lastName,
            request.gender,
            request.birthday,
            request.dailyCalorieLimit
        )
        await userEntity.update(updatedUser)

        console.log('User is successfully updated')

        return UpdateUserResponse.updateUserResponse(request.username);
    } catch (error) {
        logError(error)
        return UpdateUserResponse.internalErrorResponse();
    }
}

/**
 *  Checks if the requester has permission for this request.
 *
 * @param {DeleteUserRequest} request - DeleteUserRequest object.
 * @param {Role} role - Requesters role.
 * @returns {Promise<boolean>} - Returns Promise with true if the requester has permission and false otherwise.
 */
async function hasPermission(request, role) {
    if (request.username === request.senderUsername) return true
    return ALLOWED_ROLES.has(role)
}