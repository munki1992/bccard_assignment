package com.bccard.munki.di.network.service

import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit

private inline fun <reified T> Scope.createApi(): T = get<Retrofit>().create(T::class.java)

val apiServiceModule = module { }
