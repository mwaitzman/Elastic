package net.merayen.elastic.backend.architectures.llvm

import java.nio.ByteBuffer

class LLVMCommunicator(private val backend: LLVMBackend, private val handler: Handler) { // Is this bullshit?
	interface Handler {
		fun onReceive(data: MessagePackage)
	}

	@Volatile
	var running = true

	init {
		try {
			val incoming = ByteArray(1024)
			while (running) {
				if (backend.inputStream.available() > 0) {
					val read = backend.inputStream.read(incoming)
					if (read > 0) {

					}
					Thread.sleep(10) // TODO don't do this? Possible to "select"?
				}
			}
		} catch (exception: java.net.UnknownHostException) {
			throw RuntimeException(exception)
		} catch (exception: java.io.IOException) {
			throw RuntimeException(exception)
		}
	}

	@Synchronized
	fun send(data: MessagePackage) {
		backend.outputStream.write(data.dump())
	}

	/**
	 * Waits for receiving a complete MessagePacket from the subprocess.
	 */
	fun poll(): MessagePackage {

		val incoming = ByteArray(1024)
		backend.inputStream.read(incoming)
	}
}

open class Message(val size: Int) {
	val data: ByteBuffer = ByteBuffer.allocate(Int.SIZE_BYTES + size)

	init {
		data.putInt(size)
	}
}

class MessagePackage(val messages: List<Message> = ArrayList()) {
	fun dump(): ByteArray {
		for (x in messages) { // Prepare and validate messages
			x.data.rewind()

			if (x.data.limit() < 4)
				throw RuntimeException("Message ${x::class.simpleName} must have more than 4 bytes of data")

			val size = x.data.int
			if (x.data.limit() - 4 != size)
				throw RuntimeException("Message ${x::class.simpleName} has wrong size set (header says $size but is ${x.data.limit()})")

			x.data.rewind()
		}

		val totalSize: Int = messages.sumBy { it.data.limit() }

		val buffer = ByteBuffer.allocate(Int.SIZE_BYTES + totalSize)
		buffer.putInt(totalSize)

		for (message in messages)
			buffer.put(message.data)

		buffer.rewind()
		return buffer.array()
	}
}