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

package app.oneecom.core.network.repositiories

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import app.oneecom.core.extentions.toMD5
import app.oneecom.core.network.responses.BaseResponse
import app.oneecom.core.network.responses.Category
import app.oneecom.core.network.responses.Product
import app.oneecom.core.network.services.GithubService

// need to pick them from [local.properties] file
private const val API_PUBLIC_KEY = "daab098c007865963e16cbb4f9cef8aa"
private const val API_PRIVATE_KEY = "20b299205a9c6bca48daf7bfd3bed4b8a818e66b"
private const val HASH_FORMAT = "%s%s%s"

/**
 * Repository module for handling marvel API network operations [GithubService].
 */
class GithubRepository(
    @VisibleForTesting(otherwise = PRIVATE)
    internal val service: GithubService
) {

    /**
     * Get all Categories by paged.
     *
     * @param offset Skip the specified number of resources in the result set.
     * @param limit Limit the result set to the specified number of resources.
     * @return Response for comic characters resource.
     */
    suspend fun getCategories(offset: Int = 0, limit: Int = 0): BaseResponse<Category> {
        val timestamp = System.currentTimeMillis().toString()
        return service.getCategories(
            apiKey = API_PUBLIC_KEY,
            hash = generateApiHash(timestamp),
            timestamp = timestamp,
            offset = offset,
            limit = limit
        )
    }

    /**
     * Get all details (including products) of specified category.
     *
     * @return Response for single category resource.
     */
    suspend fun getCategory(id: Long): BaseResponse<Product> {
        val timestamp = System.currentTimeMillis().toString()
        return service.getCategory(
            id = id,
            apiKey = API_PUBLIC_KEY,
            hash = generateApiHash(timestamp),
            timestamp = timestamp
        )
    }

    // ============================================================================================
    //  Private generators methods
    // ============================================================================================

    /**
     * Generate a md5 digest of the timestamp parameter, private API key and public.
     *
     * @param timestamp A digital current record of the time.
     * @return The MD5 Hash
     */
    private fun generateApiHash(timestamp: String) =
        HASH_FORMAT.format(timestamp, API_PRIVATE_KEY, API_PUBLIC_KEY).toMD5()
}
