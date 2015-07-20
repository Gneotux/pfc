package router

import java.lang.annotation.Annotation

import javax.ws.rs.Path
import com.wordnik.swagger.annotations._
import model.{ Speaker, Atendee, User, Activity }
import spray.routing._

/**
 * Created by gneotux on 17/07/15.
 */
@Api(value = "/activities", description = "Operations for activities.", consumes= "application/json",  produces = "application/json")
trait ActivityRouterDoc {

  @ApiOperation(value = "Get a activity by id", httpMethod = "GET", response = classOf[Activity])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity that needs to retrieved", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Activity not found")
  ))
  def readRouteActivity: Route

  @ApiOperation(value = "Get all the activities", httpMethod = "GET", response = classOf[List[Activity]])
  def readAllRouteActivity: Route

  @ApiOperation(value = "Get all the atendees in a activity by activityId", httpMethod = "GET", response = classOf[User])
  @Path("/{activityId}/atendees")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Activity not found")
  ))
  def readAllAtendeesInActivity: Route

  @ApiOperation(value = "Get all the speakers in a activity by activityId", httpMethod = "GET", response = classOf[User])
  @Path("/{activityId}/speakers")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 404, message = "Activity not found")
  ))
  def readAllSpeakersInActivity: Route

  @ApiOperation(value = "Delete a activity by id", httpMethod = "DELETE", response = classOf[Int])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity that needs to be deleted", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Activity not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteActivity: Route


  @ApiOperation(value = "Add a new activity to the system", httpMethod = "POST", consumes="application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value="Activity object to be added", required = true, dataType = "router.dto.ActivityDto", paramType = "body" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid activity"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteActivity: Route

  @ApiOperation(value = "Add a new atendee for the Activity", httpMethod = "POST", response = classOf[Atendee])
  @Path("/{activityId}/speakers/{userId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "userId", value="ID of the user", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid activity"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteActivityAtendee: Route

  @ApiOperation(value = "Add new speaker for the activity", httpMethod = "POST", response = classOf[Speaker])
  @Path("/{activityId}/speakers/{userId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "userId", value="ID of the user", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 405, message = "Invalid activity"),
    new ApiResponse(code = 201, message = "Entity Created")
  ))
  def postRouteActivitySpeaker: Route

  @ApiOperation(value = "Remove an atendee for the Activity", httpMethod = "DELETE", response = classOf[Int])
  @Path("/{activityId}/speakers/{userId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "userId", value="ID of the user", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Entity not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteActivityAtendee: Route

  @ApiOperation(value = "Remove a speaker for the activity", httpMethod = "DELETE", response = classOf[Int])
  @Path("/{activityId}/speakers/{userId}")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "activityId", value="ID of the activity", required = true, dataType = "integer", paramType = "path" ),
    new ApiImplicitParam(name = "userId", value="ID of the user", required = true, dataType = "integer", paramType = "path" )
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Entity not found"),
    new ApiResponse(code = 400, message = "Invalid ID supplied")
  ))
  def deleteRouteActivitySpeaker: Route

}
