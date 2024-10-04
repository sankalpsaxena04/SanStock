package com.sandeveloper.sanstock.presentation.company_info

import com.sandeveloper.sanstock.domain.model.CompanyInfo
import com.sandeveloper.sanstock.domain.model.IntradayInfo

data class  CompanyInfoState(
    val stockInfos:List<IntradayInfo> = emptyList(),
    val companyInfo: CompanyInfo? = null,
    val isLoading:Boolean = false,
    val error:String? = null

)