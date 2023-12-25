package kittens.datasource

import com.badoo.reaktive.maybe.Maybe

internal interface KittenDataSource {

    fun load(limit: Int, page: Int): Maybe<String>
}