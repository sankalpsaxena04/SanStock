package com.sandeveloper.sanstock.di

import com.sandeveloper.sanstock.data.csv.CSVParser
import com.sandeveloper.sanstock.data.csv.CompanyListingParser
import com.sandeveloper.sanstock.data.csv.IntradayInfoParser
import com.sandeveloper.sanstock.data.repository.StockRepositoryImpl
import com.sandeveloper.sanstock.domain.model.CompanyListing
import com.sandeveloper.sanstock.domain.model.IntradayInfo
import com.sandeveloper.sanstock.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingParser: CompanyListingParser
    ):CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ):CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}