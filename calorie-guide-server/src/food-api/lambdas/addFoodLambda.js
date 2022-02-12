'use strict'

const AddFoodRequest = require('../../model/api-request/AddFoodRequest')
const AddFoodResponse = require('../../model/api-response/AddFoodResponse')
const FoodEntity = require('../../databse/FoodEntity')
const UserEntity = require('../../databse/UserEntity')
const {Role} = require('../../model/Role')
const Food = require('../../model/Food')
const ErrorCode = require('../../model/api-response/Response').ErrorCode
const {logError} = require('../../utils/errorUtils')
const DeleteFoodResponse = require("../../model/api-response/DeleteFoodResponse");

const ALLOWED_ROLES = new Set([Role.ADMIN])
/**
 * Add food Lambda function handler for responding to POST food API requests.
 *
 * @param {Object} event - Lambda event.
 * @returns {Promise<Response>} - Returns REST API response.
 */
module.exports.handler = async (event) => {
    console.log("Received event:\n", JSON.stringify(event))

    let request

    try {
        request = new AddFoodRequest(event)
        console.log("Received request:\n", JSON.stringify(request))

        request.verify()
    } catch (error) {
        logError(error)
        return AddFoodResponse.badRequestResponse(ErrorCode.INVALID_REQUEST, error.message)
    }

    if (!await hasPermission(request)) {
        return AddFoodResponse.forbiddenResponse()
    }

    try {
        const userEntity = new UserEntity(process.env.userTable)
        const user = await userEntity.get(request.username)

        if (!user) {
            console.error(`Username ${request.username} not found`)
            return AddFoodResponse.userNotFound(request.username)
        }

        const food = new Food(undefined, request.username, request.name, request.timestamp,
            request.calories)
        const foodEntity = new FoodEntity(process.env.foodTable)
        await foodEntity.put(food)

        console.log('Successfully added a new food entry')

        return AddFoodResponse.createdResponse(food)
    } catch (error) {
        logError(error)
        return AddFoodResponse.internalErrorResponse()
    }
}

/**
 *  Checks if the requester has permission for this request.
 *
 * @param {AddFoodRequest} request - AddFoodRequest object.
 * @returns {Promise<boolean>} - Returns Promise with true if the requester has permission and false otherwise.
 */
async function hasPermission(request) {
    if (request.username === request.senderUsername) return true

    const userEntity = new UserEntity(process.env.userTable)
    const role = await userEntity.getRole(request.senderUsername)
    return ALLOWED_ROLES.has(role)
}