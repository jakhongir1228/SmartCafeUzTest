package kittens.datasource

import com.badoo.reaktive.maybe.Maybe
import smartCafe.model.SmartDataResponse

internal interface KittenDataSource {

    fun load(limit: Int, page: Int): Maybe<SmartDataResponse>
}