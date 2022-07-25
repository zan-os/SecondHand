package id.co.secondhand.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.co.secondhand.data.remote.response.buyer.toDomain
import id.co.secondhand.domain.model.buyer.Product
import id.co.secondhand.utils.Constants.INITIAL_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MarketPagingSource @Inject constructor(
    private val api: MarketApi,
    private val query: String,
    private val categoryId: Int?
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: INITIAL_PAGE_INDEX

        return try {
            val response = api.getProducts(query, position, params.loadSize, categoryId)
            val photos = response.map { it.toDomain() }

            LoadResult.Page(
                data = photos,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}