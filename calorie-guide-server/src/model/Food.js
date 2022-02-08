'use strict'

const {v4: uuidv4} = require('uuid');

/**
 * Data class for Food objects from DynamoDB food table.
 */
class Food {

    /**
     * Constructor for Food class.
     *
     * @param {string} id
     * @param {string} username - User's username.
     * @param {string} name - Food name.
     * @param {number} timestamp - Timestamp of the food.
     * @param {number} calories - Number of calories for the food.
     */
    constructor(id, username, name, timestamp, calories) {
        this.id = id ? id : uuidv4()
        this.username = username
        this.name = name
        this.timestamp = timestamp
        this.calories = calories
        Object.freeze(this)
    }
}

module.exports = Food