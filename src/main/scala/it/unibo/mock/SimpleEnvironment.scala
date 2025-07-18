package it.unibo.mock

import it.unibo.core.Environment
import it.unibo.core.DistanceEstimator.*
import it.unibo.utils.Position.{*, given}

/**
 * A simple environment class used to test the framework.
 *
 * @param positions A map of node IDs to their positions.
 * @param neighboursRadius The radius within which nodes are considered neighbors.
 * @param directions A map of node IDs to their directions, defaulting to 0.0.
 */
class SimpleEnvironment(
    var positions: Map[ID, Position],
    neighboursRadius: Double,
    var directions: Map[ID, Double] = Map.empty.withDefault(_ => 0.0)
) extends Environment[ID, Position, Info]:

  override def nodes: Set[ID] = positions.keySet

  override def position(id: ID): (Double, Double) = positions(id)

  override def sensing(id: ID): Info = directions(id)
  
  override def neighbors(id: ID): Set[ID] =
    positions.filter { case (k, v) => positions(id).distance(v) <= neighboursRadius }.keys.toSet
