package smartCafe.datasource

import com.badoo.reaktive.maybe.Maybe

internal interface SmartDataSource {
    fun load(action_id:Int,user_id:Int): Maybe<String>
}