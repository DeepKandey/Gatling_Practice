package simulations


import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DataFeederCsv extends Simulation {

  val httpConf = http.baseUrl("https://gorest.co.in/")
    .header("Authorization", "Bearer 1f5a865becbd65002017a93d341940a1dee9f0794fd3b85e190ab2ed150e6e6a")

  val csvFeeder = csv("./src/test/resources/data/getUser.csv").circular

  def getAUser() = {
    repeat(7) {
      feed(csvFeeder)
        .exec(http("Get single user request")
          .get("public-api/users/${userid}")
          .check(jsonPath("$.data.name").is("${name}")).check(status.in(200, 304)))
        .pause(2)

    }
  }

  val scn = scenario("CSV FEEDER test").exec(getAUser())

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)
}
