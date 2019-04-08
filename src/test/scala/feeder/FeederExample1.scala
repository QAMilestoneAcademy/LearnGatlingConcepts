package feeder

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FeederExample1 extends Simulation {

  val state = csv("data/state.csv").circular

  val htttpProtocol = http.baseUrl("https://api.openbrewerydb.org")

  val scn = scenario("feeder example scenario")
    .during(1 minute
    ) {
      feed(state).exec(http("feeder example request").get("/breweries?by_state=${state}"))
    }
  setUp(scn.inject(atOnceUsers(4))).protocols(htttpProtocol)

}