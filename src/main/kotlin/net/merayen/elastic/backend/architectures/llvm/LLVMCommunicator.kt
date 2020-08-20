package net.merayen.elastic.backend.architectures.llvm

import java.nio.ByteBuffer

class LLVMCommunicator(private val backend: LLVMBackend, private val handler: Handler) { // Is this bullshit?
	interface Handler {
		fun onReceive(data: MessagePackage)
	}

	init {
		try {
			var incoming: ByteArray? = null
			while (true) {
				TODO()
			}
		} catch (exception: java.net.UnknownHostException) {
			throw RuntimeException(exception);
		} catch (exception: java.io.IOException) {
			throw RuntimeException(exception);
		}
	}

	@Synchronized
	fun send(data: MessagePackage) {
		backend.outputStream.write(data.dump())
	}
}

open class Message {
	var data = ByteArray(0)

	fun dump(): ByteArray {
		val buffer = ByteBuffer.allocate(Int.SIZE_BYTES + data.size)

		buffer.putInt(data.size)
		buffer.put(data)
		buffer.flip()

		return buffer.array()
	}
}

class MessagePackage(val messages: List<Message> = ArrayList()) {
	fun dump(): ByteArray {
		val result = messages.map { it.dump() }
		val totalSize: Int = result.sumBy { it.size }

		val buffer = ByteBuffer.allocate(Int.SIZE_BYTES + totalSize)
		buffer.putInt(totalSize)

		for (packet in result)
			buffer.put(packet)

		buffer.flip()
		return buffer.array()
	}
}