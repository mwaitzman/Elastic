package net.merayen.elastic.backend.architectures.llvm

import java.nio.ByteBuffer
import java.nio.ByteOrder

class LLVMCommunicator(private val backend: LLVMBackend) { // Is this bullshit?
	@Volatile
	var running = true

	private val sizeBuffer = ByteBuffer.allocate(4)

	init {
		sizeBuffer.order(ByteOrder.nativeOrder())
	}

	@Synchronized
	fun send(arr: ByteArray) {
		sizeBuffer.rewind()
		sizeBuffer.putInt(arr.size)
		sizeBuffer.rewind()

		backend.outputStream.write(sizeBuffer.array())
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
		sizeBuffer.rewind()
		backend.inputStream.read(sizeBuffer.array());
		val size = sizeBuffer.int

		if (size > 1073741824)
			throw RuntimeException("Message received from subprocess is larger than 1MB!")

		if (size < 0)
			throw RuntimeException("Negative size in package from subprocess. Data corrupt?")

		val inputBuffer = ByteBuffer.allocate(size)
		inputBuffer.order(ByteOrder.nativeOrder())

		// Read the payload
		backend.inputStream.read(inputBuffer.array())

		return inputBuffer
	}
}
