package sse

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class SSE_Example1_Stocks extends Simulation {

  val myCheck = sse.checkMessage("checkName")
    .check(regex(
      """id.*
        |data.*""").saveAs("responseBody1"))

  val httpConf = http.baseUrl("http://demo.howopensource.com")
    .acceptHeader("ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
    .inferHtmlResources()
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Gatling2")
    .upgradeInsecureRequestsHeader("1")


  val scn = scenario("Server Sent Event")

    .exec(
      sse("demoapp").connect("/sse/stocks.php")
    )
    .repeat(8) {
      exec(
        sse("SetCheck").setCheck
          .await(30 seconds)(myCheck)).exec(session => {
        println(session("responseBody1").as[String])
        session
      })
    }

    .pause(15)
    .exec(sse("Close").close())

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}
