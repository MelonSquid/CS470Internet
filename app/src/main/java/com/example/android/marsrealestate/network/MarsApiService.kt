/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()) //For Moshi's annotations to work properly with Kotlin
    .build()

/** Retrofit needs at least two things available to it to build a web services API:
 * the base URI for the web service, and a converter factory.
 * The converter tells Retrofit what to do with the data it gets back from the web service.
 * In this case, you want Retrofit to fetch a JSON response from the web service, and return it as a String.
 * Retrofit has a ScalarsConverter that supports strings and other primitive types,
 * so you call addConverterFactory() on the builder with an instance of ScalarsConverterFactory.
 * Finally, you call build() to create the Retrofit object.
 * */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/** define an interface that defines how Retrofit talks to the web server using HTTP requests
 *
 * Right now the goal is to get the JSON response string from the web service, and you only need one
 * method to do that: getProperties(). To tell Retrofit what this method should do,
 * use a @GET annotation and specify the path, or endpoint, for that web service method.
 * In this case the endpoint is called realestate. When the getProperties() method is invoked,
 * Retrofit appends the endpoint realestate to the base URL (which you defined in the Retrofit builder),
 * and creates a Call object. That Call object is used to start the request.
 * */
interface MarsApiService {
    @GET("realestate")
    suspend fun getProperties():
            List<MarsProperty>
}

/** define a public object called MarsApi to initialize the Retrofit service.
 * This is a standard Kotlin code pattern to use when creating a service object.
 *
 * The Retrofit create() method creates the Retrofit service itself with the MarsApiService interface.
 *
 * Because this call is computationally expensive, you lazily initialize the Retrofit service.
 * And since the app only needs one Retrofit service instance, you expose the service to the rest
 * of the app using a public object called MarsApi. Now once all the setup is done, each time your
 * app calls MarsApi.retrofitService, it will get a singleton Retrofit object that implements MarsApiService.
 * */
object MarsApi {
    val retrofitService : MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java) }
}