package it.unibo.mock

/**
 * Trait representing a strategy to determine if an entity should survive.
 */
trait KillStrategy:
  /**
   * Determines if the entity with the given ID should survive.
   * @param id the ID of the entity
   * @return true if the entity should survive, false otherwise
   */
  def shouldSurvive(id: ID): Boolean

object KillStrategy:
  /**
   * Creates a KillStrategy that randomly decides survival based on the given probability.
   * @param prob the probability of survival
   * @return a KillStrategy that randomly decides survival
   */
  def random(prob: Double): KillStrategy = (id: ID) => scala.util.Random.nextDouble() > prob

  /**
   * Creates a KillStrategy that always allows survival.
   * @return a KillStrategy that always returns true
   */
  def never: KillStrategy = (id: ID) => true

  /**
   * Creates a KillStrategy that kills entities after a specified number of steps.
   * @param n the number of steps after which entities are killed
   * @param who the set of IDs of entities to be killed after n steps
   * @return a KillStrategy that kills specified entities after n steps
   */
  def killIdAfterNSteps(n: Int, who: Set[ID]): KillStrategy = new KillStrategy:
    private var steps = 0

    /**
     * Determines if the entity with the given ID should survive based on the number of steps.
     * @param id the ID of the entity
     * @return true if the entity should survive, false otherwise
     */
    override def shouldSurvive(id: ID): Boolean =
      steps += 1
      steps < n || !who.contains(id)
