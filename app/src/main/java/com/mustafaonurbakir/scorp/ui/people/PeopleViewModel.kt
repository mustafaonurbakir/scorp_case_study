package com.mustafaonurbakir.scorp.ui.people

import android.os.Handler
import android.os.Looper
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.mustafaonurbakir.scorp.data.*
import com.mustafaonurbakir.scorp.data.entities.Errors

class PeopleViewModel @ViewModelInject constructor(
    private val dataSource: DataSource
) : ViewModel() {

    var people: MutableLiveData<List<Person>> = MutableLiveData()
    var loadingState : MutableLiveData<Boolean> = MutableLiveData()
    var generalErrors: MutableLiveData<Errors> = MutableLiveData()
    var paginationNo:String? = null

    private var completionHandler: FetchCompletionHandler = { fetchResponse, fetchError ->
        if(fetchResponse != null) {
            paginationNo = fetchResponse.next

            // There is no people in the list
            if (fetchResponse.people.isEmpty()) {
                generalErrors.postValue(Errors.NO_DATA)
                Handler().postDelayed({
                    fetchData()
                }, TRY_AGAIN_TIMER)

                // first meaningful data is arrived
            } else if (people.value.isNullOrEmpty()) {
                people.postValue(fetchResponse.people.groupBy { it.id }.entries.map { it.value.last() })

                // add new people to list
            } else {
                val newList = people.value?.plus(fetchResponse.people)
                    ?.groupBy { it.id }?.entries?.map { it.value.last() }

                // if there is no new people to show, print a error toast
                if (newList?.size ?: 0 != people.value!!.size) {
                    people.postValue(newList)
                } else {
                    generalErrors.postValue(Errors.NO_NEW_PERSON)
                }
            }
        }
        if(fetchError != null) {
            generalErrors.postValue(Errors.NO_DATA)
            Handler().postDelayed({
                fetchData()
            }, TRY_AGAIN_TIMER)
        }

        loadingState.value = false
    }

    init {
        fetchData()
    }

    private fun fetchData(){
        loadingState.value = true
        dataSource.fetch(paginationNo, completionHandler)
    }

    fun loadNextPage() {
        fetchData()
    }

    fun refreshPage(){
        paginationNo = null
        people.value = null
        fetchData()
    }

    companion object {
        const val TRY_AGAIN_TIMER: Long = 3000
    }


}
