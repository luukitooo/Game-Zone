package com.lukaarmen.data.repository

import com.lukaarmen.data.local.dao.FavoriteLeaguesDao
import com.lukaarmen.data.local.entity.FavoriteLeagueEntity
import com.lukaarmen.domain.model.FavoriteLeagueDomain
import com.lukaarmen.domain.repository.FavoriteLeaguesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteLeaguesRepositoryImpl @Inject constructor(
    private val dao: FavoriteLeaguesDao
): FavoriteLeaguesRepository {

    override suspend fun addLeague(league: FavoriteLeagueDomain) {
        dao.addLeague(
            FavoriteLeagueEntity.fromFavoriteLeagueDomain(league)
        )
    }

    override suspend fun removeLeague(league: FavoriteLeagueDomain) {
        dao.removeLeague(
            FavoriteLeagueEntity.fromFavoriteLeagueDomain(league)
        )
    }

    override fun getLeaguesAsFlow(): Flow<List<FavoriteLeagueDomain>> {
        return dao.getAsFlow().map { entities ->
            entities.map { entity ->
                entity.toFavoriteLeagueDomain()
            }
        }
    }

    override fun getLeaguesAsFlowByUid(uid: String): Flow<List<FavoriteLeagueDomain>> {
        return dao.getAsFlowByUid(uid).map { entities ->
            entities.map { entity ->
                entity.toFavoriteLeagueDomain()
            }
        }
    }

    override suspend fun getAllLeagues(): List<FavoriteLeagueDomain> {
        return dao.getAllLeagues().map { entity ->
            entity.toFavoriteLeagueDomain()
        }
    }

    override suspend fun getLeaguesByUid(uid: String): List<FavoriteLeagueDomain> {
        return dao.getLeaguesByUid(uid).map { entity ->
            entity.toFavoriteLeagueDomain()
        }
    }

    override suspend fun getLeagueById(id: Int): FavoriteLeagueDomain {
        return dao.getLeagueById(id).toFavoriteLeagueDomain()
    }

    override suspend fun getLeaguesByTitle(title: String): List<FavoriteLeagueDomain> {
        return dao.getLeaguesByTitle(title).map { entity ->
            entity.toFavoriteLeagueDomain()
        }
    }

}