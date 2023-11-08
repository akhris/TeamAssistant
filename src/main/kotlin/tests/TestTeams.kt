package tests

import domain.Team

val testTeam1 = Team(
    name = "CMOS department"
)

val testTeam2 = Team(
    name = "Bipolar department"
)

val testTeam3 = Team(
    name = "IC department",
    childTeams = listOf(testTeam1, testTeam2)
)