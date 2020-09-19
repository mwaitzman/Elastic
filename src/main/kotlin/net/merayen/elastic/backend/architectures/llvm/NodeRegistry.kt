package net.merayen.elastic.backend.architectures.llvm

import net.merayen.elastic.backend.architectures.llvm.nodes.*

val nodes = mapOf(
	"group" to GroupTranspilerNode::class,
	"value" to ValueTranspilerNode::class,
	"elapsed" to ElapsedTranspilerNode::class,
	"multiply" to MultiplyTranspilerNode::class,
	"sine" to SineTranspilerNode::class,
	"out" to OutTranspilerNode::class
)