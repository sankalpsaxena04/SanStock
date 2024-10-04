package com.sandeveloper.sanstock.data.repository

import android.util.Log
import coil.network.HttpException
import com.sandeveloper.sanstock.data.csv.CSVParser
import com.sandeveloper.sanstock.data.local.StockDatabase
import com.sandeveloper.sanstock.data.mapper.toCompanyInfo
import com.sandeveloper.sanstock.data.mapper.toCompanyListing
import com.sandeveloper.sanstock.data.mapper.toCompanyListingEntity
import com.sandeveloper.sanstock.data.remote.StockApi
import com.sandeveloper.sanstock.domain.model.CompanyInfo
import com.sandeveloper.sanstock.domain.model.CompanyListing
import com.sandeveloper.sanstock.domain.model.IntradayInfo
import com.sandeveloper.sanstock.domain.repository.StockRepository
import com.sandeveloper.sanstock.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class StockRepositoryImpl @Inject constructor(
    private val api:StockApi,private val db: StockDatabase
    ,private val companyListingParser: CSVParser<CompanyListing>
    ,private val intradayInfoParser: CSVParser<IntradayInfo>
): StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
            return flow {
                emit(Resource.Loading(true))
                val localListings = dao.search(query)
                emit(Resource.Success(localListings.map { it.toCompanyListing() }))

                val isDbEmpty = localListings.isEmpty() && query.isBlank()
                val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

                if(shouldJustLoadFromCache){
                    emit(Resource.Loading(false))
                    return@flow
                }
                val remoteListings = try {
                    val response = api.getListings()
                    Log.d("apiResponse", response.byteString().toString())
                    companyListingParser.parse(response.byteStream())
                }catch (e: IOException){
                    e.printStackTrace()
                    emit(Resource.Error("Couldn't load data"))
                    null
                }catch (e:HttpException){
                    e.printStackTrace()
                    emit(Resource.Error("Couldn't load data"))
                    null
                }

                remoteListings?.let { listings->
                    dao.clearCompanyListing()
                    dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })
                    emit(
                        Resource.Success(
                        data = dao.search("").map { it.toCompanyListing()}
                    ))
                    emit(Resource.Loading(false))
                }


            }

    }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
                val response = api.getIntraDatInfo(symbol = symbol)
                val result = intradayInfoParser.parse(response.byteStream())
                Resource.Success(result)
        }
        catch (e:IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        }
        catch (e:HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )

        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val resource = api.getCompanyInfo(symbol).toCompanyInfo()
            Resource.Success(resource)
        }
        catch (e:IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )
        }
        catch (e:HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )
        }
    }
}