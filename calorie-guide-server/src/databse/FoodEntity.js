'use strict'

const dynamoDb = require('./dynamoDb')
const {logError} = require('../utils/errorUtils')

/**
 * Class for performing operation on the DynamoDB food table.
 */
class FoodEntity {

    /**
     * Constructor for FoodEntity class.
     *
     * @param {string} tableName - DynamoDB food table name.
     * @param {string} [tableIndex] - DynamoDB food table index name. Necessary only for query by username operation.
     */
    constructor(tableName, tableIndex) {
        this.tableName = tableName
        this.tableIndex = tableIndex
        Object.freeze(this)
    }

    /**
     * Gets Food object from food DynamoDB table with id.
     *
     * @param {string} id - Food ID
     * @returns {Promise<Food|undefined>} - Returns Food object or undefined.
     */
    get = async (id) => {
        const params = {
            TableName: this.tableName,
            Key:{
                id
            }
        }

        console.log('Getting Food object from DynamoDB...')

        try {
            const result = await dynamoDb.get(params)
            return result.Item
        } catch (error) {
            logError(error)
            return
        }
    }

    /**
     * Gets all food entries.
     *
     * @return {Promise<Food[]>} - Returns array of Food objects.
     */
    getAllFood = async () => {
        const params = {
            TableName: this.tableName
        }

        try {
            return await dynamoDb.scanAll(params)
        } catch (error) {
            logError(error)
            return []
        }
    }

    /**
     * Gets all user's food entries.
     *
     * @param {string} username
     * @param {number} [from] - From timestamp
     * @param {number} [to] - To timestamp
     * @return {Promise<Food[]>} - Returns array of Food objects.
     */
    getUserFood = async (username, from, to) => {
        const params = {
            TableName: this.tableName,
            KeyConditionExpression: "username = :username",
            ExpressionAttributeValues: {
                ":username": username
            },
            IndexName: this.tableIndex
        }

        if (from && to) {
            params.KeyConditionExpression += " and #timestamp between :from and :to"
            params.ExpressionAttributeNames = {
                "#timestamp": "timestamp"
            }
            params.ExpressionAttributeValues[":from"] = from
            params.ExpressionAttributeValues[":to"] = to
        } else if (from) {
            params.KeyConditionExpression += " and #timestamp >= :from"
            params.ExpressionAttributeNames = {
                "#timestamp": "timestamp"
            }
            params.ExpressionAttributeValues[":from"] = from
        } else if (to) {
            params.KeyConditionExpression += " and #timestamp <= :to"
            params.ExpressionAttributeNames = {
                "#timestamp": "timestamp"
            }
            params.ExpressionAttributeValues[":to"] = to
        }

        try {
            return await dynamoDb.query(params)
        } catch (error) {
            logError(error)
            return []
        }
    }

    /**
     * Puts new Food object to the food DynamoDB table.
     *
     * @param {Food} Food - Food object.
     * @returns {Promise<DocumentClient.PutItemOutput>}
     */
    put = async (Food) => {
        const params = {
            TableName: this.tableName,
            Item: Food,
            ConditionExpression: 'attribute_not_exists(id)'
        }

        console.log('Adding Food object to DynamoDB...')

        return dynamoDb.put(params)
    }

    /**
     * Updates Food object in the food DynamoDB table.
     *
     * @param {Food} Food - Food object.
     * @returns {Promise<DocumentClient.PutItemOutput>}
     */
    update = async (Food) => {
        const params = dynamoDb.createUpdateParams(this.tableName, 'id', undefined, Food)
        console.log('Updating Food object in DynamoDB...')
        return dynamoDb.update(params)
    }

    /**
     * Deletes Food object from the table.
     *
     * @param {string} id - Food ID.
     * @returns {Promise<DocumentClient.DeleteItemOutput>}
     */
    delete = async (id) => {
        console.log(`Deleting ${id} food...`)
        const params = {
            TableName: this.tableName,
            Key: {
                id
            }
        }
        return dynamoDb.deleteItem(params)
    }
}

module.exports = FoodEntity