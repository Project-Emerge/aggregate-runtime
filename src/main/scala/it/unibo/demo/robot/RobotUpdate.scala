package it.unibo.demo.robot

import it.unibo.core.{Environment, EnvironmentUpdate}
import it.unibo.demo.robot.Actuation.{Forward, NoOp, Rotation}
import it.unibo.demo.{ID, Info, Position}

import scala.concurrent.{ExecutionContext, Future}

/**
 * ADT representing the actuation of a robot.
 * That are, Rotation (turn the robot in a specific direction),
 * Forward (move the robot in a specific direction) and NoOp (do nothing).
 */
enum Actuation:
  case Rotation(rotationVector: (Double, Double))
  case Forward(vector: (Double, Double))
  case NoOp

class RobotUpdate(robots: List[Robot], threshold: Double)(using ExecutionContext)
    extends EnvironmentUpdate[ID, Position, Actuation, Info, Environment[ID, Position, Info]]:

  override def update(world: Environment[ID, Position, Info], id: ID, actuation: Actuation): Future[Unit] =
    val robot = robots.find(_.id == id).get
    actuation match
      case _ if !world.nodes.contains(id) => Future(robot.nop())
      case NoOp => Future(robot.nop())
      case Rotation(actuation) =>
        Future:
          val direction = world.sensing(id)
          val directionVector = (Math.cos(direction), Math.sin(direction))
          val rotationVector = (actuation._2, actuation._1)
          var deltaAngle =
            math.atan2(rotationVector._2, rotationVector._1) - Math.atan2(directionVector._2, directionVector._1)
          if (deltaAngle > math.Pi)
            deltaAngle = deltaAngle - 2 * math.Pi
          else if (deltaAngle < -math.Pi)
            deltaAngle = deltaAngle + 2 * math.Pi
          val angleEuclideanDistance = Math.sqrt(
            (rotationVector._1 - directionVector._1) * (rotationVector._1 - directionVector._1) +
              (rotationVector._2 - directionVector._2) * (rotationVector._2 - directionVector._2)
          )
          angleEuclideanDistance match
            case _ if angleEuclideanDistance < threshold => robot.nop()
            case _ if deltaAngle > 0 => robot.spinRight()
            case _ => robot.spinLeft()
      case Forward(actuation) =>
        Future:
          val direction = world.sensing(id)
          val directionVector = (Math.cos(direction), Math.sin(direction))
          val adjustedVector = (actuation._2, actuation._1)
          val vectorDirection = Math.atan2(actuation._2, actuation._1)
          val vector = if (vectorDirection < 0) (-adjustedVector._1, -adjustedVector._2) else adjustedVector
          var deltaAngle = Math.atan2(vector._2, vector._1) - Math.atan2(directionVector._2, directionVector._1)
          if (deltaAngle > math.Pi)
            deltaAngle = deltaAngle - 2 * math.Pi
          else if (deltaAngle < -math.Pi)
            deltaAngle = deltaAngle + 2 * math.Pi
          val angleEuclideanDistance = Math.sqrt(
            (vector._1 - directionVector._1) * (vector._1 - directionVector._1) +
              (vector._2 - directionVector._2) * (vector._2 - directionVector._2)
          )
          angleEuclideanDistance match
            case _ if angleEuclideanDistance < threshold =>
              if (vectorDirection < 0) robot.backward() else robot.forward()
            case _ if deltaAngle > 0 => robot.spinRight()
            case _ => robot.spinLeft()
      // convert from -pi, pi to 0 to 2pi
  def adjustAngle(angle: Double): Double =
    if angle < 0 then angle + 2 * Math.PI else angle
