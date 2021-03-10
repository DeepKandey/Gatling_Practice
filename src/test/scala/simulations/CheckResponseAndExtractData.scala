package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CheckResponseAndExtractData extends Simulation {

  val httpConf = http.baseUrl("https://gorest.co.in/")
    .header("Authorization", "Bearer 1f5a865becbd65002017a93d341940a1dee9f0794fd3b85e190ab2ed150e6e6a")

  val scn = scenario("check Correlation and extract data")

    // first call - get all the users
    .exec(http("get all users")
      .get("public-api/users")
      .check(jsonPath("$.data[0].id").saveAs("userId")))

    .exec(http("Get specific user")
      .get("public-api/users/${userId}")
      .check(jsonPath("$.data.id").is("2"))
      .check(jsonPath("$.data.name").is("Patricia"))
      .check(jsonPath("$.data.email").is("patricia1234@gmail.com")))

  setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))

}
