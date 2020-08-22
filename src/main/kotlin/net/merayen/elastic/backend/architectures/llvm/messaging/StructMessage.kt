package net.merayen.elastic.backend.architectures.llvm.messaging

import java.nio.ByteBuffer

abstract class StructMessage {
	abstract fun dump(): ByteBuffer
	abstract fun load(data: ByteBuffer)
 }