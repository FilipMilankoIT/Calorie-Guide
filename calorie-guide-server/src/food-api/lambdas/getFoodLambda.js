'use strict'

const GetFoodListRequest = require('../../model/api-request/GetFoodListRequest')
const GetFoodListResponse = require('../../model/api-response/GetFoodListResponse')
const FoodEntity = require('../../databse/FoodEntity')
const UserEntity = require('../../databse/UserEntity')
const {Role} = require('../../model/Role')
const {logError} = require('../../utils/errorUtils')
const {ErrorCode} = require("../../model/api-response/Response");

const ALLOWED_ROLES = new Set([Role.ADMIN])

/**
 * Get food list Lambda function handler for responding to GET food API requests.
 *
 * @param {Object} event - Lambda event.
 * @returns {Promise<Response>} - Returns REST API response.
 */
module.exports.handler = async (event) => {
    console.log("Received event:\n", JSON.stringify(event))

    let request

    try {
        request = new GetFoodListRequest(event)
        console.log("Received request:\n", JSON.stringify(request))

        request.verify()
    } catch (error) {
        logError(error)
        return GetFoodListResponse.badRequestResponse(ErrorCode.INVALID_REQUEST, error.message)
    }

    if (!await hasPermission(request)) {
        return GetFoodListResponse.forbiddenResponse()
    }

    try {
        const foodEntity = new FoodEntity(process.env.foodTable, process.env.foodTableIndex)

        const username = request.username ? request.username : request.senderUsername

        const result = await foodEntity.getUserFood(username, request.from, request.to)

        console.log('Successfully retrieved user\'s food entries:\n', JSON.stringify(result))

        return GetFoodListResponse.okResponse(result)
    } catch (error) {
        logError(error)
        return GetFoodListResponse.internalErrorResponse()
    }
}

/**
 *  Checks if the requester has permission for this request.
 *
 * @param {GetFoodListRequest} request - GetFoodListRequest object.
 * @returns {Promise<boolean>} - Returns Promise with true if the requester has permission and false otherwise.
 */
async function hasPermission(request) {
    if (!request.username || request.username === request.senderUsername) return true

    const userEntity = new UserEntity(process.env.userTable)
    const role = await userEntity.getRole(request.senderUsername)
    return ALLOWED_ROLES.has(role)
}