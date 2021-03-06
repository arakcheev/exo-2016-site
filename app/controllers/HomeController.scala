package controllers

import java.io.File
import java.net.URLDecoder
import java.util.jar.JarFile
import javax.inject._

import akka.stream.scaladsl.StreamConverters
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream
import models.ProgramPdf
import play.api._
import play.api.cache.{CacheApi, Cached}
import play.api.http.HttpEntity
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.matching.Regex

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(digestAssetLoader: DigestAssetLoader, cached: Cached, programPdf: ProgramPdf) extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index(url: String) = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /**
    * Get js variable with all assets templates. In development mode this variable contains just the same named tempalte,
    * but in production mode this variable contains md5-digests assets templates file (and may be gzipped).
    *
    */
  def templates = Action { implicit req =>
    val templatesJson = digestAssetLoader.cacheTemplates.foldLeft(Json.obj()) { case (js, template) =>
      js ++ Json.obj(
        s"/assets/$template" -> routes.Assets.versioned(template).url
      )
    }

    val result =
      s"""
         |var templates = $templatesJson
        """.stripMargin

    Ok(result).as(JAVASCRIPT)
  }

  /**
    * Retrieves all routes via reflection.
    * http://stackoverflow.com/questions/12012703/less-verbose-way-of-generating-play-2s-javascript-router
    */
  val routeCache = {
    val jsRoutesClasses = Seq(classOf[routes.javascript])
    jsRoutesClasses.flatMap { jsRoutesClass =>
      val controllers = jsRoutesClass.getFields.map(_.get(null))
      controllers.flatMap { controller =>
        controller.getClass.getDeclaredMethods.filter(_.getName != "_defaultPrefix").map { action =>
          action.invoke(controller).asInstanceOf[play.api.routing.JavaScriptReverseRoute]
        }
      }
    }
  }

  /**
    * Returns the JavaScript router that the client can use for "type-safe" routes.
    * Uses browser caching; set duration (in seconds) according to your release cycle.
    */
  def jsRoutes = Action { implicit request =>
    Ok(play.api.routing.JavaScriptReverseRouter("jsRoutes")(routeCache: _*)).as(JAVASCRIPT)
  }


  def downloadProgram(name: String = "exo-school-program.pdf") = Action.async {
    programPdf.get().map { bytes =>
      val in = new ByteInputStream(bytes, bytes.length)
      val source = StreamConverters.fromInputStream(() => in)
      Result(
        ResponseHeader(200,
          Map(
            CONTENT_DISPOSITION -> {
              val dispositionType = "attachment"
              dispositionType + "; filename=\"" + name + "\""
            }
          )
        ),
        HttpEntity.Streamed(
          source,
          Some(bytes.length),
          play.api.libs.MimeTypes.forFileName(name).orElse(Some(play.api.http.ContentTypes.BINARY))
        )
      )
    }
  }
}

private class DigestAssetLoader @Inject()(cache: CacheApi, environment: Environment) {

  /** Get list of available templates from asset-jar file. */
  def cacheTemplates: List[String] = cache.getOrElse[List[String]]("templates") {
    Logger.debug("Get templates")

    if (environment.mode == Mode.Dev) {
      def loop(f: File, r: Regex): Array[File] = {
        val these = f.listFiles
        val good = these.filter(f => r.findFirstIn(f.getName).isDefined)
        good ++ these.filter(_.isDirectory).flatMap(loop(_, r))
      }

      loop(new File("app/assets/templates/"), """.*\.html$""".r).map(_.getPath.replace("app/assets/", "")).toList
    } else {
      val url = environment.resource("/public/templates/").getOrElse(sys.error("WTF???? Where is assets jar??? "))

      val jarPath = url.getPath.substring(5, url.getPath.indexOf("!"))

      val jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))

      val entries = jar.entries()

      val list = collection.mutable.Set.empty[String]

      while (entries.hasMoreElements) {
        val name = entries.nextElement().getName
        if (name.endsWith(".html.md5")) {
          list.add(name)
        }
      }
      list.toList.map { name =>
        name.replaceAll("public/templates/", "templates/").replace(".html.md5", ".html")
      }
    }

  }
}