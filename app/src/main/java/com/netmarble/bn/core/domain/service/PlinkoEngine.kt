package com.netmarble.bn.core.domain.service

import androidx.compose.ui.geometry.Offset
import com.netmarble.bn.core.domain.model.PhysicsConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

data class Peg(val center: Offset)
data class Slot(val x: Float, val multiplier: Double)

class PlinkoEngine {

    fun generatePegs(width: Float, height: Float): List<Peg> {
        val rows = 9
        val cols = 7
        val top = height * 0.08f
        val bottom = height * 0.18f
        val usable = height - top - bottom
        val rowStep = usable / rows
        val colStep = width / (cols + 1)
        val out = ArrayList<Peg>()
        var y = top
        repeat(rows) { r ->
            val offset = if (r % 2 == 0) 0f else colStep / 2f
            var x = colStep + offset
            repeat(cols) {
                out.add(Peg(Offset(x, y)))
                x += colStep
            }
            y += rowStep
        }
        return out
    }

    fun generateSlots(width: Float): List<Slot> {
        val multipliers = listOf(10.0, 3.0, 0.5, 3.0, 10.0)
        val n = multipliers.size
        val segment = width / n
        return List(n) { i ->
            Slot(
                x = segment * i + segment / 2f,
                multiplier = multipliers[i]
            )
        }
    }

    suspend fun run(
        cfg: PhysicsConfig,
        width: Float,
        height: Float,
        pegs: List<Peg>,
        slots: List<Slot>,
        rng: Random,
        onPosition: (Offset) -> Unit,
        onVelocity: (Offset) -> Unit,
        onFinish: (Int) -> Unit
    ) = withContext(Dispatchers.Main) {
        val pegRadius = min(width, height) * cfg.pegRadiusFactor
        val ballRadius = pegRadius * cfg.ballRadiusFactor
        val minDist = ballRadius + pegRadius

        val wallInset = 0.8f
        val minKickBase = width * 0.0016f
        val minKickVyFactor = 0.12f
        val edgeRepelRange = width * 0.025f
        val edgeRepelStrength = width * 0.0065f

        var pos = Offset(width * 0.5f, ballRadius + 2f)
        var vel = Offset.Zero

        while (coroutineContext.isActive) {
            awaitFrame()

            repeat(cfg.substeps) {
                vel = Offset(
                    vel.x * cfg.damping,
                    (vel.y + height * cfg.gravityFactor) * cfg.damping
                )
                pos += vel

                if (pos.x < ballRadius) {
                    pos = Offset(ballRadius + wallInset, pos.y)
                    val kick = maxOf(minKickBase, abs(vel.y) * minKickVyFactor)
                    vel = Offset(kick, vel.y)
                } else if (pos.x > width - ballRadius) {
                    pos = Offset(width - ballRadius - wallInset, pos.y)
                    val kick = maxOf(minKickBase, abs(vel.y) * minKickVyFactor)
                    vel = Offset(-kick, vel.y)
                } else {
                    if (pos.x < ballRadius + edgeRepelRange) {
                        val t = (ballRadius + edgeRepelRange - pos.x) / edgeRepelRange
                        vel = vel.copy(x = vel.x + edgeRepelStrength * t)
                    } else if (pos.x > width - ballRadius - edgeRepelRange) {
                        val t = (pos.x - (width - ballRadius - edgeRepelRange)) / edgeRepelRange
                        vel = vel.copy(x = vel.x - edgeRepelStrength * t)
                    }
                }

                var hit = false
                for (peg in pegs) {
                    val dx = pos.x - peg.center.x
                    val dy = pos.y - peg.center.y
                    val dist = sqrt(dx * dx + dy * dy)
                    if (dist < minDist && dist > 0.0001f) {
                        val nx = dx / dist
                        val ny = dy / dist

                        val dot = vel.x * nx + vel.y * ny
                        var rvx = vel.x - 2f * dot * nx
                        var rvy = vel.y - 2f * dot * ny

                        val restitution = cfg.restitutionMin +
                                rng.nextFloat() * (cfg.restitutionMax - cfg.restitutionMin)
                        rvx *= restitution
                        rvy *= restitution

                        val tx = -ny; val ty = nx
                        val tangentKick = (width * cfg.tangentKickFactor) * (rng.nextFloat() * 2f - 1f)
                        rvx += tx * tangentKick
                        rvy += ty * tangentKick

                        val normalBoost = abs(dot) * cfg.normalBoostFactor + (width * 0.0008f)
                        rvx += nx * normalBoost
                        rvy += ny * normalBoost

                        val angle = (
                                (cfg.angleMinDeg + rng.nextFloat() * (cfg.angleMaxDeg - cfg.angleMinDeg))
                                        * (Math.PI / 180.0)
                                ).toFloat() * (if (rng.nextBoolean()) 1f else -1f)
                        val c = kotlin.math.cos(angle)
                        val s = kotlin.math.sin(angle)
                        val rx = rvx * c - rvy * s
                        val ry = rvx * s + rvy * c

                        vel = Offset(rx, ry)

                        val push = (minDist - dist) + 0.5f
                        pos = Offset(pos.x + nx * push, pos.y + ny * push)
                        hit = true
                    }
                }

                if (hit && abs(vel.x) < width * 0.00045f) {
                    vel = vel.copy(x = vel.x + (width * 0.0022f) * (rng.nextFloat() * 2f - 1f))
                }

                if (pos.y > height - ballRadius - 2f) {
                    onFinish(pickSlot(slots, pos.x))
                    return@withContext
                }
            }

            onPosition(pos)
            onVelocity(vel)
        }
    }

    private fun pickSlot(slots: List<Slot>, x: Float): Int {
        if (slots.isEmpty()) return 0
        var best = 0
        var bestDist = Float.MAX_VALUE
        slots.forEachIndexed { i, s ->
            val d = abs(x - s.x)
            if (d < bestDist) {
                bestDist = d
                best = i
            }
        }
        return best
    }
}