package simulations

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UpdateUserSimulation extends Simulation {

  var httpConf = http.baseUrl("https://reqres.in")
    .header("Accept", "application/json")
    .header("content-type", "application/json")

  val scn = scenario("Update User Scenario")

    .exec(http("Update specific user")
      .put("/api/users/2")
      .body(RawFileBody(".src/test/resources/bodies/UpdateUser.json")).asJson
      .check(status.in(200 to 201)))

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)
}
