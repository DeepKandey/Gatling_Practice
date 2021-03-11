package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class FixedDurationLoadSimulation extends Simulation {

  val httpConf = http.baseUrl("https://reqres.in/")
    .header("Accept", "application/json")
    .header("content-type", "application/json")

  def getAllUsersRequest() = {
    repeat(2) {
      exec(http("get all users request")
        .get("api/users?page=2")
        .check(status.is(200)))
    }
  }

  def getAUserRequest() = {
    repeat(2) {
      exec(http("get single user request")
        .get("api/users/2")
        .check(status.is(200)))
    }
  }

  def addUser() = {
    repeat(2) {
      exec(http("add a user request")
        .post("api/users")
        .body(RawFileBody("./src/test/resources/bodies/AddUser.json")).asJson
        .check(status.is(201)))
    }
  }

  val scn = scenario("Fixed Duration Load Simulation")
    .forever() {
      exec(getAllUsersRequest())
        .pause(2)
        .exec(getAUserRequest())
        .pause(2)
        .exec(addUser())
    }

  setUp(
    scn.inject(
      nothingFor(5),
      atOnceUsers(10),
      rampUsers(50) during (30 seconds)
    ).protocols(httpConf)).maxDuration(1 minute)
}
