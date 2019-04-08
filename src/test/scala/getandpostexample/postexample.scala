package getandpostexample

import io.gatling.http.Predef._
import io.gatling.core.Predef._


class postexample extends Simulation {

  //Create http configuration
  val httpProtocol = http.baseUrl("https://api.rebrandly.com")
    .headers(Map("Content-Type" -> "application/json", "apikey" -> "9d02943b6f854cd893ea667e6a5a40ac"))

  //Create scenario

  val scn = scenario("CreateLink")
    .exec(http("createnewlink").post("/v1/links").body(StringBody("""{"destination":"https://www.qamilestone.com/"}""")).asJson) //send post request

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
