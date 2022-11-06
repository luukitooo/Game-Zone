package com.lukaarmen.domain.model

data class MatchPlayersDomain(
    var firstTeamPlayer: TeamDomain.PlayerDomain? = null,
    var secondTeamPlayer: TeamDomain.PlayerDomain? = null
)
