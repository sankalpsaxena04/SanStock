package com.sandeveloper.sanstock.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.sanstock.domain.repository.StockRepository
import com.sandeveloper.sanstock.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(private val repository: StockRepository):ViewModel() {

    var state by mutableStateOf(CompanyListingsState())
    private var searchJob: Job? = null

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingsEvent){
        when(event){
            is CompanyListingsEvent.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingsEvent.OnSearchQueryChange -> {        //this is triggered for every single character we type
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()                                 //if we already have a search job we cancel it
                searchJob = viewModelScope.launch {                 //relaunch a new search job
                    //to reduce the number of search queries and making the search efficient we add a 500ms delay
                    delay(500L)
                    getCompanyListings(fetchFromRemote = false)
                }
            }
        }
    }

    private fun getCompanyListings(
        query:String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ){
        viewModelScope.launch {
            repository.getCompanyListing(fetchFromRemote,query)
                .collect {
                    when(it){
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            state.copy(isLoading = it.isLoading)
                        }
                        is Resource.Success -> {
                            it.data?.let { listings->
                                state = state.copy(
                                    companies = listings
                                )
                            }
                        }
                    }
                }
        }
    }
}