package com.finevotech.orderservicecqrs.executor

import com.yoda.mo.api.provider.executor.MarketOrderExecutor

interface ExecutorConfigurer {
    fun marketOrderExecutor(resourceId: String): MarketOrderExecutor
}