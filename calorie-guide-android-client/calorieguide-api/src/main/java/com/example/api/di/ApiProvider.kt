package com.example.api.di

import com.example.api.CalorieGuideApi
import com.example.api.CalorieGuideApiImpl

object ApiProvider {

    fun getAuthApi(baseUrl: String): CalorieGuideApi {
        return CalorieGuideApiImpl(baseUrl)
    }
}