package net.merayen.elastic.backend.architectures.llvm

import java.nio.ByteBuffer
import java.nio.ByteOrder

class LLVMCommunicator(private val backend: LLVMBackend) { // Is this bullshit?
	@Volatile
	var running = true

	private val inputBuffer = ByteBuffer.allocate(4)

	init {
		inputBuffer.order(ByteOrder.nativeOrder())
	}

	@Synchronized
	fun send(message: Message) {
		val arr = message.data.array()
		backend.outputStream.write(arr)
		backend.outputStream.flush()
	}

	/**
	 * Waits for receiving a complete MessagePacket from the subprocess.
	 * Waits until the complete package has been received.
	 * Returns the payload of the packet (also, not including the header that states the packet size).
	 *
	 * WARNING: The buffer returned is owned by this class. Copy it if you need to use the data before next poll()-call.
	 */
	@Synchronized
	fun poll(): ByteBuffer {

		// Read an int for the package size
		if (inputBuffer.limit() < 4)
			inputBuffer.limit(4)

		backend.inputStream.read(inputBuffer.array());
		inputBuffer.rewind()

		val size = inputBuffer.int

		if (size > 1073741824)
			throw RuntimeException("Message received from subprocess is larger than 1GB!")

		if (size < 0)
			throw RuntimeException("Negative size in package from subprocess. Data corrupt?")

		println("Resizing inputBuffer to $size")
		inputBuffer.limit(size)

		inputBuffer.rewind()

		// Read the payload
		backend.inputStream.read(inputBuffer.array())

		return inputBuffer
	}
}

open class Message(val size: Int) {
	val data: ByteBuffer = ByteBuffer.allocate(Int.SIZE_BYTES + size)

	init {
		data.order(ByteOrder.nativeOrder())
		data.putInt(size)
	}
}