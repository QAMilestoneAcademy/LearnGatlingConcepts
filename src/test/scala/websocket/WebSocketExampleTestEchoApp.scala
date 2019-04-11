package websocket
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class WebSocketExampleTestEchoApp extends Simulation {

  val httpConf = http.baseUrl("http://websocket.org")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Gatling")
    .wsBaseUrl("ws://echo.websocket.org")

  val scn = scenario("WebSocketScenario").exec(http("openhomepage").get("/"))
    .pause(1 second)
    .exec(ws("opensocket").connect("/?encoding=text"))
    .pause(2 seconds)
    .exec(ws("send message").sendText("hello")
      .await(20 seconds)
      (ws.checkTextMessage("hellocheck").check(regex(".*hello.*").saveAs("name"))))
    .exec(session => {

      val mystring= session("name").as[String]
      println("hello"+ mystring)
      session
    })
    .pause(1 second)
    .exec(ws("close socket").close)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)


}
