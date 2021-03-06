package router

import router.dto.ActivityDto
import service.ActivityService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing.{ HttpService, Route }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

/**
 * Created by gneotux on 17/07/15.
 */
trait ActivityRouter extends HttpService with ActivityRouterDoc {
  self: Authenticator =>

  val activityService: ActivityService

  val activityOperations: Route =
    postRouteActivity ~
    readRouteActivity ~
    readAllRouteActivity ~
    deleteRouteActivity ~
    updateRouteActivity ~
    readAllAttendeesInActivity ~
    readAllSpeakersInActivity ~
    postRouteActivityAttendee ~
    postRouteActivitySpeaker ~
    deleteRouteActivityAttendee ~
    deleteRouteActivitySpeaker ~
    readAllTagsInActivity ~
    postRouteActivityTag ~
    deleteRouteActivityTag


  override def readRouteActivity = path("activities" / IntNumber) { activityId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.get(activityId)) {
            case Success(Some(activity)) => complete(activity)
            case Success(None) => complete(NotFound, "Activity not found")
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def readAllRouteActivity = path("activities") {
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.getAll) {
            case Success(activities) => complete(activities)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def deleteRouteActivity = path("activities" / IntNumber) { activityId =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(activityService.delete(activityId)) {
              case Success(activityId: Int) => complete(OK, activityId.toString)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  override def updateRouteActivity: Route = path("activities" / IntNumber) { activityId =>
    put {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          entity(as[ActivityDto]) { activity =>
            respondWithMediaType(`application/json`) {
              onComplete(activityService.update(activityId, activity)) {
                case Success(Some(newActivity)) => complete(OK, newActivity)
                case Success(None) => complete(NotAcceptable, "Invalid activity")
                case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }
  }

  override def postRouteActivity: Route = path("activities") {
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          entity(as[ActivityDto]) { activity =>
            respondWithMediaType(`application/json`) {
              onComplete(activityService.add(activity)) {
                case Success(Some(newActivity)) => complete(Created, newActivity)
                case Success(None) => complete(NotAcceptable, "Invalid activity")
                case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
              }
            }
          }
        }
      }
    }
  }

  override def readAllAttendeesInActivity = path("activities" / IntNumber / "attendees") { activityId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.getAllAttendees(activityId)) {
            case Success(atendees) => complete(atendees)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def postRouteActivityAttendee: Route = path("activities" / IntNumber / "attendees" / IntNumber) { (activityId, userId) =>
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(activityService.addAttendee(activityId, userId)) {
              case Success(atendee) => complete(Created, atendee)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  override def deleteRouteActivityAttendee: Route = path("activities" / IntNumber / "attendees" / IntNumber) { (activityId, userId) =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(activityService.deleteAttendee(activityId, userId)) {
              case Success(atendeeId: Int) => complete(OK, atendeeId.toString)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }


  override def readAllSpeakersInActivity = path("activities" / IntNumber / "speakers") { activityId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.getAllSpeakers(activityId)) {
            case Success(speakers) => complete(speakers)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def postRouteActivitySpeaker: Route = path("activities" / IntNumber / "speakers"/ IntNumber) { (activityId, userId) =>
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(activityService.addSpeaker(activityId, userId)) {
              case Success(speaker) => complete(Created, speaker)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }


  override def deleteRouteActivitySpeaker: Route = path("activities" / IntNumber / "speakers"/ IntNumber) { (activityId, userId) =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(activityService.deleteSpeaker(activityId, userId)) {
              case Success(speakerId: Int) => complete(OK, speakerId.toString)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  override def readAllTagsInActivity = path("activities" / IntNumber / "tags") { activityId =>
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(activityService.getAllTags(activityId)) {
            case Success(tags) => complete(tags)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }

  override def postRouteActivityTag: Route = path("activities" / IntNumber / "tags"/ IntNumber) { (activityId, tagId) =>
    post {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(activityService.addTag(activityId, tagId)) {
              case Success(activityTag) => complete(Created, activityTag)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  override def deleteRouteActivityTag: Route = path("activities" / IntNumber / "tags"/ IntNumber) { (activityId, tagId) =>
    delete {
      authenticate(basicUserAuthenticator) { authInfo =>
        authorize(authInfo.hasPermissions("ADMIN")) {
          respondWithMediaType(`application/json`) {
            onComplete(activityService.deleteTag(activityId, tagId)) {
              case Success(activityTagId: Int) => complete(OK, activityTagId.toString)
              case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }


}
