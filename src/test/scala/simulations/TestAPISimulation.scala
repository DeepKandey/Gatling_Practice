package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class TestAPISimulation extends Simulation {

  //http conf
  val httpConf = http.baseUrl("http://reqres.in/")
    .header("Accept", "application/json")
    .header("content", "application/json")

  //scenario
  val scn = scenario("get user")
    .exec(http("get user request")
      .get("api/users/2")
      .check(status is 200)
    )

  //setup
  setUp(scn.inject(atOnceUsers(1000))).protocols(httpConf)

}