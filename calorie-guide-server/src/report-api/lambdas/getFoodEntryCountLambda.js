'use strict'

const GetFoodEntryCountRequest = require('../../model/api-request/GetFoodEntryCountRequest')
const GetFoodEntryCountResponse = require('../../model/api-response/GetFoodEntryCountResponse')
const FoodEntity = require('../../databse/FoodEntity')
const UserEntity = require('../../databse/UserEntity')
const Role = require('../../model/Role').Role
const ErrorCode = require('../../model/api-response/Response').ErrorCode
const {logError} = require('../../utils/errorUtils')

const ALLOWED_ROLES = new Set([Role.ADMIN])

/**
 * Get food entry count Lambda function handler for responding to GET report/food/count API requests.
 *
 * @param {Object} event - Lambda event.
 * @returns {Promise<Response>} - Returns REST API response.
 */
module.exports.handler = async (event) => {
    console.log("Received event:\n", JSON.stringify(event))

    let request

    try {
        request = new GetFoodEntryCountRequest(event)
        console.log("Received request:\n", JSON.stringify(request))

        request.verify()
    } catch (error) {
        logError(error)
        return GetFoodEntryCountResponse.badRequestResponse(ErrorCode.INVALID_REQUEST, error.message)
    }

    if (!await hasPermission(request)) {
        return GetFoodEntryCountResponse.forbiddenResponse()
    }

    try {
        const foodEntity = new FoodEntity(process.env.foodTable)
        const count = (await foodEntity.getAllFood(request.from, request.to)).length

        console.log('Successfully retrieved food entry count:', count)

        return GetFoodEntryCountResponse.okResponse(count)
    } catch (error) {
        logError(error)
        return GetFoodEntryCountResponse.internalErrorResponse()
    }
}

/**
 *  Checks if the requester has permission for this request.
 *
 * @param {GetFoodEntryCountRequest} request - GetFoodEntryCountRequest object.
 * @returns {Promise<boolean>} - Returns Promise with true if the requester has permission and false otherwise.
 */
async function hasPermission(request) {
    const userEntity = new UserEntity(process.env.userTable)
    const role = await userEntity.getRole(request.senderUsername)
    return ALLOWED_ROLES.has(role)
}