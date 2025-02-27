package it.unibo.mock

trait KillStrategy {
  def shouldSurvive(id: ID): Boolean
}

object KillStrategy {
  def random(prob: Double): KillStrategy = new KillStrategy {
    override def shouldSurvive(id: ID): Boolean = scala.util.Random.nextDouble() > prob
  }

  def never: KillStrategy = new KillStrategy {
    override def shouldSurvive(id: ID): Boolean = true
  }

  def killIdAfterNSteps(n: Int, who: Set[ID]): KillStrategy = new KillStrategy {
    private var steps = 0
    override def shouldSurvive(id: ID): Boolean = {
      steps += 1
      steps < n || !who.contains(id)
    }
  }
}
