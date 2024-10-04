package com.sandeveloper.sanstock.domain.repository

import com.sandeveloper.sanstock.domain.model.CompanyInfo
import com.sandeveloper.sanstock.domain.model.CompanyListing
import com.sandeveloper.sanstock.domain.model.IntradayInfo
import com.sandeveloper.sanstock.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListing(fetchFromRemote: Boolean, query: String): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntraDayInfo(symbol: String): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo>
}