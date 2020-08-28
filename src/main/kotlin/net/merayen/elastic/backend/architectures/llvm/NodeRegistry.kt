package net.merayen.elastic.backend.architectures.llvm

import net.merayen.elastic.backend.architectures.llvm.nodes.*

val nodes = mapOf(
	"group" to GroupCNode::class,
	"value" to ValueCNode::class,
	"elapsed" to ElapsedCNode::class,
	"multiply" to MultiplyCNode::class,
	"sine" to SineCNode::class,
	"out" to OutCNode::class
)