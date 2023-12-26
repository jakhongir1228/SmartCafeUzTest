package smartCafe.model


data class SmartDataResponse(
	val data: SmartData? = null
)

data class SmartData(
	val authorization: Any? = null,
	val resultCode: Int? = null,
	val resultNote: String? = null,
	val actionID: Int? = null,
	val items: Any? = null,
	val userID: Int? = null
)
