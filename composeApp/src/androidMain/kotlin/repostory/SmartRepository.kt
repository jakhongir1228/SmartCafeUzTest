package repostory

import android.content.Context
import api.ApiService
import dataNetwork.ApiEndPoint
import network.ApiClient
import network.DataManager
import sharedPrefHelper.PrefHelper
import prefHelper.SharedPref
import retrofit2.Response
import smartCafe.model.SmartDataResponse
import smartCafe.model.SmartRequest

class SmartRepository: DataManager {
    private var mContext: Context? = null
    private var apiService: ApiService? = null
    private var prefHelper: PrefHelper? = null

    constructor(context: Context?) {
        mContext = context
        val apiClient = ApiClient(mContext, ApiEndPoint.BASE_URL)
        apiService = apiClient.getApiClient()!!.create(ApiService::class.java)
        prefHelper = context?.let { SharedPref(it) }
    }

    override suspend fun requestSmartPlace(actionId: Int, userId: Int, body: SmartRequest): Response<SmartDataResponse> {
        return apiService!!.requestSmartPlace(actionId,userId,body)
    }

    override fun saveLogin(login: String?) {
        prefHelper?.saveLogin(login)
    }

    override fun getLogin(): String? {
        return prefHelper?.getLogin()
    }

    override fun savePassword(password: String?) {
        prefHelper?.savePassword(password)
    }

    override fun getPassword(): String? {
        return prefHelper?.getPassword()
    }
}