package it.unibo.mock

import it.unibo.core.EnvironmentUpdate
import it.unibo.demo.robot.Actuation.*

import scala.concurrent.Future

class SimpleUpdate(killStrategy: KillStrategy = KillStrategy.never)
    extends EnvironmentUpdate[ID, Position, Actuation, Info, SimpleEnvironment]:

  override def update(world: SimpleEnvironment, id: ID, actuation: Actuation): Future[Unit] = Future.successful:
    
    val position = world.positions(id)
    actuation match
      case NoOp => // world.positions = world.positions.updated(id, position)
      case Rotation(actuation) =>
        world.positions = world.positions.updated(id, (position._1, position._2))
      case Forward(actuation) =>
        if (actuation._1.isNaN || actuation._2.isNaN) {} else
          world.positions = world.positions.updated(id, (position._1 + actuation._1, position._2 + actuation._2))
    // update direction
    val newPosition = world.positions(id)
    val direction = Math.atan2(newPosition._2 - position._2, newPosition._1 - position._1)
    // rotate 180
    world.directions = world.directions.updated(id, direction + (Math.PI / 2))
    if(!killStrategy.shouldSurvive(id)) world.positions = world.positions.removed(id)
    ()
