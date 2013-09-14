package sim.cloth

import sim.glutil.Vector3DUtil._

class SpringDamper(p1: Particle, p2: Particle, stiffness: Float, damping: Float) {

  val restLength = (p1.getPosition - p2.getPosition).length

  private def getForces(xi: Vector3D, vi: Vector3D, xj: Vector3D, vj: Vector3D): (Vector3D, Vector3D) = {
    val diffPosition = xi - xj
    val diffPositionUnit = diffPosition.normalize
    val diffVelocity = vi - vj

    val distanceBetween = diffPosition.length

    val springTerm = -stiffness / restLength * (distanceBetween - restLength)
    val dampingTerm = -damping / restLength * (diffVelocity dot diffPositionUnit)

    val totalForce = (springTerm + dampingTerm) * diffPosition.normalize

    (totalForce, -totalForce)
  }

  private def getForces(dt: Float): (Vector3D, Vector3D) = {
    getForces(p1.getPosition, p1.getVelocity(dt), p2.getPosition, p2.getVelocity(dt))
  }

  def apply(dt: Float) = {
    val forces = getForces(dt)
    p1.applyForce(forces._1)
    p2.applyForce(forces._2)
  }

}