package com.lukaarmen.gamezone.model

import com.lukaarmen.domain.model.LeaguesDomain
import com.lukaarmen.domain.model.MatchDomain
import com.lukaarmen.domain.model.MatchPlayersDomain
import com.lukaarmen.domain.model.TeamDomain
import com.lukaarmen.domain.model.VideoGameDomain

//Matches
fun MatchDomain.toMatch() = Match(
    beginAt = beginAt,
    draw = draw,
    endAt = endAt,
    games = games?.map { it?.toGame() },
    id = id,
    matchType = matchType,
    modifiedAt = modifiedAt,
    name = name,
    numberOfGames = numberOfGames,
    opponents = opponents?.map { it?.toTeam() },
    results = results?.map { it?.toResult() },
    slug = slug,
    status = status,
    streamsList = streamsList?.map { it?.toStream() },
    videoGame = videoGame?.toVideoGame(),
    winner = winner?.toTeam()
)

fun MatchDomain.GameDomain.toGame() = Match.Game(
    beginAt = beginAt,
    complete = complete,
    endAt = endAt,
    finished = finished,
    id = id,
    length = length,
    matchId = matchId,
    position = position,
    status = status,
    winnerId = winnerId
)

fun MatchDomain.ResultDomain.toResult() = Match.Result(
    score = score,
    teamId = teamId
)

fun MatchDomain.StreamDomain.toStream() = Match.Stream(
    embedUrl = embedUrl,
    language = language,
    main = main,
    official = official,
    rawUrl = rawUrl
)

fun TeamDomain.toTeam() = Team(
    acronym = acronym,
    currentVideoGame = currentVideoGame?.toVideoGame(),
    id = id,
    imageUrl = imageUrl,
    location = location,
    modifiedAt = modifiedAt,
    name = name,
    players = players?.map { it?.toPlayer() },
    slug = slug
)

fun VideoGameDomain.toVideoGame() = VideoGame(
    id = id,
    name = name,
    slug = slug
)

fun TeamDomain.PlayerDomain.toPlayer() = Team.Player(
    age = age,
    birthday = birthday,
    firstName = firstName,
    id = id,
    imageUrl = imageUrl,
    lastName = lastName,
    name = name,
    nationality = nationality,
    role = role,
    slug = slug
)

fun LeaguesDomain.toLeague() = League(
    id = id,
    imageUrl = imageUrl,
    name = name,
    url = url
)

fun MatchPlayersDomain.toMatchPlayers() = MatchPlayers(
    firstTeamPlayer = firstTeamPlayer?.toPlayer(),
    secondTeamPlayer = secondTeamPlayer?.toPlayer()
)