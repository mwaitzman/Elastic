package net.merayen.elastic.backend.architectures.llvm.nodes

import net.merayen.elastic.backend.architectures.llvm.messaging.NodeDataMessage
import java.nio.ByteBuffer

class Math1 : BaseLLVMNode() {
	class DataStruct(nodeId: String) : NodeDataMessage(nodeId) {
		override fun load(data: ByteBuffer) {
			TODO("Not yet!")
		}
	}
}