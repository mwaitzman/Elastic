package net.merayen.elastic.backend.architectures.llvm.nodes

abstract class BaseCNode {
	class Environment( // TODO should this be inherited? Wat? Can't be global?
		val sampleRate: Int
	) {
	}
	val nodeId: String
		get() = TODO()

	abstract fun onGenerateC(): String
}