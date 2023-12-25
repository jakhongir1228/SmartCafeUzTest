package smartCafe.model


data class DataResponse(
	val data: Data? = null
)

data class Data(
	val authorization: Any? = null,
	val resultCode: Int? = null,
	val resultNote: String? = null,
	val actionID: Int? = null,
	val items: Any? = null,
	val userID: Int? = null
)
