package network

import api.ApiService
import sharedPrefHelper.PrefHelper

interface DataManager: ApiService, PrefHelper {
}