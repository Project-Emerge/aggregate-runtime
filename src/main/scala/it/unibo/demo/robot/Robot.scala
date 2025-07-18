package it.unibo.demo.robot

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import java.net.URLEncoder
import upickle.default.{macroRW, ReadWriter as RW, *}

/** A robot that can be controlled through basic movement commands.
 * The robot can move forward, backward, spin left/right, and accepts direct motor control through intensities.
 */
trait Robot:
  /** @return The unique identifier of the robot */
  def id: Int
  /** Spins the robot clockwise */
  def spinRight(): Unit
  /** Spins the robot counter-clockwise */
  def spinLeft(): Unit
  /** Moves the robot forward */
  def forward(): Unit
  /** Moves the robot backward */
  def backward(): Unit
  /** Stops the robot's movement */
  def nop(): Unit
  /** Controls the robot's motors directly through intensity values
   * @param left The intensity for the left motor (-1.0 to 1.0)
   * @param right The intensity for the right motor (-1.0 to 1.0)
   */
  def intensities(left: Double, right: Double): Unit

/** Implementation of a Robot that communicates over HTTP with wave platform
 * @param ip The IP address of the robot
 * @param id The unique identifier of the robot
 */
class WaveRobot(ip: String, val id: Int) extends Robot:
  private var lastCommandWasNoOp = false
  /** Spins the robot clockwise by setting opposite motor intensities */
  def spinRight(): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(0.20, -0.20).toJson}")
    lastCommandWasNoOp = false
  /** Spins the robot counter-clockwise by setting opposite motor intensities */
  def spinLeft(): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(-0.20, 0.20).toJson}")
    lastCommandWasNoOp = false
  /** Moves the robot forward by setting equal negative intensities */
  def forward(): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(-0.26, -0.26).toJson}")
    lastCommandWasNoOp = false
  /** Moves the robot backward by setting equal positive intensities */
  def backward(): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(0.26, 0.26).toJson}")
    lastCommandWasNoOp = false
  /** Sets the motor intensities directly
   * @param left The intensity for the left motor (-1.0 to 1.0)
   * @param right The intensity for the right motor (-1.0 to 1.0)
   */
  def intensities(left: Double, right: Double): Unit =
    requests.get(url = s"http://$ip/js?json=${Command(left, right).toJson}")
    lastCommandWasNoOp = false
  /** Stops the robot by setting both motor intensities to 0.
   * Avoids sending redundant stop commands.
   */
  override def nop(): Unit =
    if !lastCommandWasNoOp then requests.get(url = s"http://$ip/js?json=${Command(0, 0).toJson}")
    else ()
    lastCommandWasNoOp = true
  /** Helper class for constructing JSON commands
   * @param left The left motor intensity
   * @param right The right motor intensity
   */
  private class Command(left: Double, right: Double):
    /** @return URL-encoded JSON string representing the command */
    def toJson: String = URLEncoder.encode(s"""{"T":1, "L":$left, "R":$right}""")


object RobotMqttProtocol:
  // TODO Create a provide
  val client = MqttClient("tcp://localhost:1883", MqttClient.generateClientId())
  client.connect()
  case class RobotMovement(left: Double, right: Double)
  given RW[RobotMovement] = macroRW

  def spinRight(robot: Int): Unit =
    client.publish(s"robots/${robot}/move", write(RobotMovement(0.2, -0.2)).getBytes, 0, false)

  def spinLeft(robot: Int): Unit =
    client.publish(s"robots/${robot}/move", write(RobotMovement(-0.2, 0.2)).getBytes, 0, false)

  def nop(robot: Int): Unit =
    client.publish(s"robots/${robot}/move", write(RobotMovement(0, 0)).getBytes, 0, false)

  def forward(robot: Int): Unit =
    client.publish(s"robots/${robot}/move", write(RobotMovement(0.9, 0.9)).getBytes, 0, false)

  def backward(robot: Int): Unit =
    client.publish(s"robots/${robot}/move", write(RobotMovement(-0.9, -0.9)).getBytes, 0, false)


