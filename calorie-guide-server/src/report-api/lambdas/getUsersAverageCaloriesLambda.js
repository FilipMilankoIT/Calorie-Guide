'use strict'

const GetUsersAverageCaloriesRequest = require('../../model/api-request/GetUsersAverageCaloriesRequest')
const GetUsersAverageCaloriesResponse = require('../../model/api-response/GetUsersAverageCaloriesResponse')
const FoodEntity = require('../../databse/FoodEntity')
const UserEntity = require('../../databse/UserEntity')
const Role = require('../../model/Role').Role
const ErrorCode = require('../../model/api-response/Response').ErrorCode
const {logError} = require('../../utils/errorUtils')

const ALLOWED_ROLES = new Set([Role.ADMIN])

/**
 * Get users average calories Lambda function handler for responding to GET report/user/calories/average API requests.
 *
 * @param {Object} event - Lambda event.
 * @returns {Promise<Response>} - Returns REST API response.
 */
module.exports.handler = async (event) => {
    console.log("Received event:\n", JSON.stringify(event))

    let request

    try {
        request = new GetUsersAverageCaloriesRequest(event)
        console.log("Received request:\n", JSON.stringify(request))

        request.verify()
    } catch (error) {
        logError(error)
        return GetUsersAverageCaloriesResponse.badRequestResponse(ErrorCode.INVALID_REQUEST, error.message)
    }

    if (!await hasPermission(request)) {
        return GetUsersAverageCaloriesResponse.forbiddenResponse()
    }

    try {
        const items = []

        const userEntity = new UserEntity(process.env.userTable)
        const foodEntity = new FoodEntity(process.env.foodTable, process.env.foodTableIndex)

        const result = await userEntity.getAllUsers(undefined, request.limit, request.exclusiveStartKey)
        for(const user of result.items) {
            let average = await foodEntity.getUserAverageCalories(user.username, request.from, request.to)
            items.push(new GetUsersAverageCaloriesResponse.AverageUserCalories(user.username, average))
        }

        console.log('Successfully retrieved users average calorie count:', items)

        return GetUsersAverageCaloriesResponse.okResponse(items, result.lastEvaluatedKey)
    } catch (error) {
        logError(error)
        return GetUsersAverageCaloriesResponse.internalErrorResponse()
    }
}

/**
 *  Checks if the requester has permission for this request.
 *
 * @param {GetUsersAverageCaloriesRequest} request - GetUsersAverageCaloriesRequest object.
 * @returns {Promise<boolean>} - Returns Promise with true if the requester has permission and false otherwise.
 */
async function hasPermission(request) {
    const userEntity = new UserEntity(process.env.userTable)
    const role = await userEntity.getRole(request.senderUsername)
    return ALLOWED_ROLES.has(role)
}