package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.language.postfixOps

import scala.concurrent.duration.DurationInt

class RampUsersLoadSimulation extends Simulation {

  val httpConf = http.proxy(Proxy("localhost", 8866)).baseUrl("https://gorest.co.in/")
    .header("Authorization", "Bearer 1f5a865becbd65002017a93d341940a1dee9f0794fd3b85e190ab2ed150e6e6a")

  val csvFeeder = csv("./src/test/resources/data/getUser.csv").circular

  def getAUser() = {
    repeat(1) {
      feed(csvFeeder)
        .exec(http("Get single user request")
          .get("public-api/users/${userid}")
          .check(jsonPath("$.data.name").is("${name}")).check(status.in(200, 304)))

    }
  }

  val scn = scenario("Ramp Users load simulation").exec(getAUser())

  setUp(
    scn.inject(
      nothingFor(5),
      constantUsersPerSec(10) during (10 seconds),
      rampUsersPerSec(1) to (5) during (20 seconds)
    ).protocols(httpConf))
}
