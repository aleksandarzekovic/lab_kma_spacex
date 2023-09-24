package me.aleksandarzekovic.labkmaspacex.shared.data.repository

import com.apollographql.apollo3.ApolloClient
import me.aleksandarzekovic.labkmaspacex.GetShipsQuery
import me.aleksandarzekovic.labkmaspacex.fragment.ShipsDetail

interface ShipsRepository {
    @Throws(Exception::class)
    suspend fun getShips(): List<ShipsDetail>
    suspend fun getShip(id: String): ShipsDetail?
}

class ShipsRepositoryImpl(
    private val apolloClient: ApolloClient
) : ShipsRepository {

    @Throws(Exception::class)
    override suspend fun getShips(): List<ShipsDetail> {
        val response = apolloClient.query(GetShipsQuery()).execute()
        return response.dataAssertNoErrors.ships?.mapNotNull {
            it?.shipsDetail
        }.orEmpty()
    }

    // We use a query for all ships to retrieve details, since the query for details by id does not work
    override suspend fun getShip(id: String): ShipsDetail? {
        val response = apolloClient.query(GetShipsQuery()).execute()
        return response.dataAssertNoErrors.ships?.mapNotNull {
            it?.shipsDetail
        }?.firstOrNull { it.id == id }
    }
}