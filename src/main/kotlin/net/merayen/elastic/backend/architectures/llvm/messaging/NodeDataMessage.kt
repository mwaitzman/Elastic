package net.merayen.elastic.backend.architectures.llvm.messaging

import java.nio.ByteBuffer

/**
 * Send or receive data from a node.
 */
abstract class NodeDataMessage(val nodeId: String) : StructMessage() {
	override fun dump(): ByteBuffer {
		val result = ByteBuffer.allocate(10)

		TODO()
	}
}