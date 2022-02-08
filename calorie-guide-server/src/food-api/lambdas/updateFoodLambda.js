'use strict'

const UpdateFoodRequest = require('../../model/api-request/UpdateFoodRequest')
const UpdateFoodResponse = require('../../model/api-response/UpdateFoodResponse')
const FoodEntity = require('../../databse/FoodEntity')
const UserEntity = require('../../databse/UserEntity')
const {Role} = require('../../model/Role')
const Food = require('../../model/Food')
const { logError } = require('../../utils/errorUtils')
const {ErrorCode} = require("../../model/api-response/Response");

const ALLOWED_ROLES = new Set([Role.ADMIN])

/**
 * Update food Lambda function handler for responding to PATCH food/{id} API requests.
 *
 * @param {Object} event - Lambda event.
 * @returns {Promise<Response>} - Returns REST API response.
 */
module.exports.handler = async (event) => {
    console.log("Received event:\n", JSON.stringify(event))

    let request

    try {
        request = new UpdateFoodRequest(event)
        console.log("Received request:\n", JSON.stringify(request))

        request.verify()
    } catch (error) {
        logError(error)
        return UpdateFoodResponse.badRequestResponse(ErrorCode.INVALID_BODY, error.message)
    }

    try {
        const foodEntity = new FoodEntity(process.env.foodTable)

        const food = await foodEntity.get(request.id)
        if(!food) {
            return UpdateFoodResponse.notFoundResponse(request.id)
        }

        if (! await hasPermission(request, food)) {
            return UpdateFoodResponse.forbiddenResponse()
        }

        const updatedFood = new Food(request.id, undefined, request.name, request.timestamp, request.calories)
        const result = await foodEntity.update(updatedFood)

        console.log('Successfully updated food entry')

        return UpdateFoodResponse.okResponse(result.Attributes)
    } catch (error) {
        logError(error)
        return UpdateFoodResponse.internalErrorResponse()
    }
}

/**
 *  Checks if the requester has permission for this request.
 *
 * @param {UpdateFoodRequest} request - UpdateFoodRequest object.
 * @param {Food} food - Requested food object to be updated.
 * @returns {Promise<boolean>} - Returns Promise with true if the requester has permission and false otherwise.
 */
async function hasPermission(request, food) {
    if (food.username === request.senderUsername)  return true

    const userEntity = new UserEntity(process.env.userTable)
    const role = await userEntity.getRole(request.senderUsername)
    return ALLOWED_ROLES.has(role)
}