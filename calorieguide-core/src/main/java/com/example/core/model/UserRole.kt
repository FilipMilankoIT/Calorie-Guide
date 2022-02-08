package com.example.core.model

enum class UserRole(val value: String) {
    USER("user"),
    ADMIN("admin");

    companion object {
        fun from(value: String?) =
            when(value) {
                ADMIN.value -> ADMIN
                else -> USER
            }
    }
}