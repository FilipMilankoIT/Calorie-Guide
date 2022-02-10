package com.example.core.model

enum class ErrorCode(val code: String) {
    UNKNOWN("Unknown"),
    UNAUTHORIZED("Unauthorized"),
    INVALID_REQUEST("InvalidRequest"),
    USERNAME_NOT_FOUND("UsernameNotFound"),
    USERNAME_ALREADY_EXISTS("UsernameAlreadyExists"),
    NO_PERMISSION("NoPermission"),
    ITEM_NOT_FOUND("ItemNotFound");

    companion object {
        fun from(code: String?) =
            when(code) {
                UNAUTHORIZED.code -> UNAUTHORIZED
                INVALID_REQUEST.code -> INVALID_REQUEST
                USERNAME_NOT_FOUND.code -> USERNAME_NOT_FOUND
                USERNAME_ALREADY_EXISTS.code -> USERNAME_ALREADY_EXISTS
                NO_PERMISSION.code -> NO_PERMISSION
                ITEM_NOT_FOUND.code -> ITEM_NOT_FOUND
                else -> UNKNOWN
            }
    }
}