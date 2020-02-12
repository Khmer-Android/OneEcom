/*
 * Copyright 2019 vmadalin.com
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
 */

package app.oneecom.core.di.modules

import app.oneecom.core.BuildConfig
import app.oneecom.core.network.repositiories.GithubRepository
import app.oneecom.core.network.services.GithubService
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * @see Module
 */
@Module
class NetworkModule {

    /**
     * Create a provider method binding for [HttpLoggingInterceptor].
     *
     * @return Instance of http interceptor.
     * @see Provides
     */
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    /**
     * Create a provider method binding for [OkHttpClient].
     *
     * @return Instance of http client.
     * @see Provides
     */
    @Singleton
    @Provides
    fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(interceptor)
            clientBuilder.addNetworkInterceptor(StethoInterceptor())
        }
        return clientBuilder.build()
    }

    /**
     * Create a provider method binding for [Retrofit].
     *
     * @return Instance of retrofit.
     * @see Provides
     */
    @Singleton
    @Provides
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    /**
     * Create a provider method binding for [GithubService].
     *
     * @return Instance of github service.
     * @see Provides
     */
    @Singleton
    @Provides
    fun provideGithubservice(retrofit: Retrofit): GithubService = retrofit
        .create(GithubService::class.java)

    /**
     * Create a provider method binding for [GithubRepository].
     *
     * @return Instance of github repository.
     * @see Provides
     */
    @Singleton
    @Provides
    fun provideGithubRepository(service: GithubService) = GithubRepository(service)
}
