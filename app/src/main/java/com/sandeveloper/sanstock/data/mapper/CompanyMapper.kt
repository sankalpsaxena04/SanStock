package com.sandeveloper.sanstock.data.mapper

import com.sandeveloper.sanstock.data.local.CompanyListingEntity
import com.sandeveloper.sanstock.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing{
    return CompanyListing(
        symbol = symbol,
        name = name,
        exchange = exchange
    )
}
fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity{
    return CompanyListingEntity(
        symbol = symbol,
        name = name,
        exchange = exchange
    )
}