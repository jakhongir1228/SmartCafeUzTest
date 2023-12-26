package api

import dataNetwork.ApiEndPoint
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import smartCafe.model.SmartDataResponse
import smartCafe.model.SmartRequest

interface ApiService {
    @POST(ApiEndPoint.AUTHORIZATION)
    suspend fun requestSmartPlace(
        @Header("action_id") actionId:Int,
        @Header("user_id")  userId:Int,
        @Body body: SmartRequest
        ):Response<SmartDataResponse>
}