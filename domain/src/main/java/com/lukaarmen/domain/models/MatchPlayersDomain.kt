package com.lukaarmen.domain.models

data class MatchPlayersDomain(
    var firstTeamPlayer: TeamDomain.PlayerDomain? = null,
    var secondTeamPlayer: TeamDomain.PlayerDomain? = null
)
