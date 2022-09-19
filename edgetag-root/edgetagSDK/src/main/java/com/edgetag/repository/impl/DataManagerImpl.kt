package com.edgetag.repository.impl

import com.edgetag.DependencyInjector
import com.edgetag.repository.data.ConfigurationDataManager
import com.edgetag.repository.data.DataManager

class DataManagerImpl(private val dependencyInjector: DependencyInjector) : DataManager {

    override fun getConfigurationDataManager(): ConfigurationDataManager {
        return ConfigurationDataManagerImpl(dependencyInjector.getRemoteAPIDataSource())
    }
}
