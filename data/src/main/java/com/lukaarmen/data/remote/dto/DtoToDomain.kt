package com.lukaarmen.data.remote.mappers

import com.lukaarmen.data.remote.dto.*
import com.lukaarmen.domain.model.*

//Leagues
fun LeaguesDto.toLeaguesDomain() = LeaguesDomain(
    id = id,
    imageUrl = imageUrl,
    modifiedAt = modifiedAt,
    name = name,
    slug = slug,
    url = url,
    videoGame = videoGame?.toVideoGameDomain()
)

//Matches
fun MatchDto.toMatchDomain() = MatchDomain(
    beginAt = beginAt,
    draw = draw,
    endAt = endAt,
    games = games?.map { it?.toGameDomain() },
    id = id,
    matchType = matchType,
    modifiedAt = modifiedAt,
    name = name,
    numberOfGames = numberOfGames,
    opponents = opponents?.map { it?.opponent?.toTeamDomain() },
    results = results?.map { it?.toResultDomain() },
    slug = slug,
    status = status,
    streamsList = streamsList?.map { it?.toStreamDomain() },
    videoGame = videogame?.toVideoGameDomain(),
    winner = winner?.toTeamDomain()
)

fun MatchDto.GameDto.toGameDomain() = MatchDomain.GameDomain(
    beginAt = beginAt,
    complete = complete,
    endAt = endAt,
    finished = finished,
    id = id,
    length = length,
    matchId = matchId,
    position = position,
    status = status,
    winnerId = winner?.id
)

fun MatchDto.ResultDto.toResultDomain() = MatchDomain.ResultDomain(
    score = score,
    teamId = teamId
)

fun MatchDto.StreamDto.toStreamDomain() = MatchDomain.StreamDomain(
    embedUrl = embedUrl,
    language = language,
    main = main,
    official = official,
    rawUrl = rawUrl
)

fun TeamDto.toTeamDomain() = TeamDomain(
    acronym = acronym,
    currentVideoGame = currentVideoGame?.toVideoGameDomain(),
    id = id,
    imageUrl = imageUrl,
    location = location,
    modifiedAt = modifiedAt,
    name = name,
    players = players?.map { it?.toPlayerDomain() },
    slug = slug
)

fun TeamDto.PlayerDto.toPlayerDomain() = TeamDomain.PlayerDomain(
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

fun VideoGameDto.toVideoGameDomain() = VideoGameDomain(
    id = id,
    name = name,
    slug = slug
)
