package net.merayen.elastic.backend.architectures.llvm.nodes

import net.merayen.elastic.backend.architectures.llvm.CWriter

abstract class CNode(env: Env) {
	class Env(
		val nodeId: String,
		val sampleRate: Int,
		val frameSize: Int
	)

	var cwriter: CWriter? = null

	val nodeId = env.nodeId
	val frameSize = env.frameSize
	val sampleRate = env.sampleRate
}