package com.lukaarmen.domain.repositories

import com.lukaarmen.domain.models.FavoriteLeagueDomain
import com.lukaarmen.domain.models.LeaguesDomain
import kotlinx.coroutines.flow.Flow

interface FavoriteLeaguesRepository {

    suspend fun addLeague(league: FavoriteLeagueDomain)

    suspend fun removeLeague(league: FavoriteLeagueDomain)

    fun getLeaguesAsFlow(): Flow<List<FavoriteLeagueDomain>>

    fun getLeaguesAsFlowByUid(uid: String): Flow<List<FavoriteLeagueDomain>>

    suspend fun getAllLeagues(): List<FavoriteLeagueDomain>

    suspend fun getLeaguesByUid(uid: String): List<FavoriteLeagueDomain>

    suspend fun getLeagueById(id: Int): FavoriteLeagueDomain

    suspend fun getLeaguesByTitle(title: String): List<FavoriteLeagueDomain>

}