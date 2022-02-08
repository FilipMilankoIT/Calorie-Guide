'use strict'

const DeleteFoodRequest = require('../../model/api-request/DeleteFoodRequest')
const DeleteFoodResponse = require('../../model/api-response/DeleteFoodResponse')
const FoodEntity = require('../../databse/FoodEntity')
const UserEntity = require('../../databse/UserEntity')
const {Role} = require('../../model/Role')
const { logError } = require('../../utils/errorUtils')
const {ErrorCode} = require("../../model/api-response/Response");

const ALLOWED_ROLES = new Set([Role.ADMIN])

/**
 * Delete food Lambda function handler for responding to DELETE food/{id} API requests.
 *
 * @param {Object} event - Lambda event.
 * @returns {Promise<Response>} - Returns REST API response.
 */
module.exports.handler = async (event) => {
    console.log("Received event:\n", JSON.stringify(event))

    let request

    try {
        request = new DeleteFoodRequest(event)
        console.log("Received request:\n", JSON.stringify(request))

        request.verify()
    } catch (error) {
        logError(error)
        return DeleteFoodResponse.badRequestResponse(ErrorCode.INVALID_REQUEST, error.message)
    }

    try {
        const foodEntity = new FoodEntity(process.env.foodTable)

        const food = await foodEntity.get(request.id)
        if(!food) {
            return DeleteFoodResponse.notFoundResponse(request.id)
        }

        if (! await hasPermission(request, food)) {
            return DeleteFoodResponse.forbiddenResponse()
        }

        await foodEntity.delete(request.id)

        console.log(`Successfully deleted ${request.id} food entry`)

        return DeleteFoodResponse.okResponse(request.id)
    } catch (error) {
        logError(error)
        return DeleteFoodResponse.internalErrorResponse()
    }
}

/**
 *  Checks if the requester has permission for this request.
 *
 * @param {DeleteFoodRequest} request - DeleteFoodRequest object.
 * @param {Food} food - Requested food object to be deleted.
 * @returns {Promise<boolean>} - Returns Promise with true if the requester has permission and false otherwise.
 */
async function hasPermission(request, food) {
    if (food.username === request.senderUsername)  return true

    const userEntity = new UserEntity(process.env.userTable)
    const role = await userEntity.getRole(request.senderUsername)
    return ALLOWED_ROLES.has(role)
}