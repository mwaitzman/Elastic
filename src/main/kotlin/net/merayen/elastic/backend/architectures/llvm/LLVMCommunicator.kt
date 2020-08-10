package net.merayen.elastic.backend.architectures.llvm

class LLVMCommunicator(private val backend: LLVMBackend, private val handler: Handler) {
	interface Handler {

	}

	private val socket = java.net.Socket("localhost", 5001) // TODO take port (and host?) as argument

	init {
		try {
			var incoming: ByteArray? = null
			sendHi()
			while (true) {
				val incomingData = socket.getInputStream().read()

				if (incomingData == null)
					break;

				//handleResult(line);
			}
		} catch (exception: java.net.UnknownHostException) {
			throw RuntimeException(exception);
		} catch (exception: java.io.IOException) {
			throw RuntimeException(exception);
		}
	}

	fun handleResult(line: String) {
		println("Java got: '" + line + "'");

		if ("H".equals(line)) {
			println("Service said hi!")
		} else {
			println("Unknown result from node")
		}
	}

	@Synchronized
	fun send(data: MessagePackage) {
		socket.getOutputStream().write(data.dump())
	}

	private fun sendHi() {
		val messagePackage = MessagePackage()
		messagePackage.messages.add(Message(MessageType.HI, MessageHiData()))
		send(messagePackage)
	}
}

fun toChars(number: Long, len: Int): ByteArray {  // TODO does this virk? How will it være over the TCP stækk?
	if (number < 0)
		throw RuntimeException("Negative not supported")

	val result = ByteArray(len)

	for (i in 0 until len)
		result[i] = ((number / if (i != len - 1) 256 * (len - i - 1) else 1) % 256).toByte()

	return result
}

enum class MessageType(val data: Char) {
	HI('H'),
	NODE_DATA('D'), // Send data to a node
	PROCESS('P'), // Ask to process a frame
	PING('p'),
	QUIT('Q');
}

interface DataObject {
	fun dump(): ByteArray
}

interface MessageData : DataObject {

}

class MessageHiData : MessageData {
	override fun dump(): ByteArray {
		return "Hi there".toByteArray()
	}
}

class Message(
	val type: MessageType,
	val data: MessageData
) : DataObject {
	override fun dump(): ByteArray {
		val messageData = data.dump()
		val result = ByteArray(4 + messageData.size)

		var i = 0
		for (c in toChars(messageData.size.toLong(), 4))
			result[i++] = c

		for (c in messageData)
			result[i++] = c

		return result
	}
}

class MessagePackage(
	val messages: ArrayList<Message> = ArrayList()
) : DataObject {
	override fun dump(): ByteArray {
		val result = ArrayList<ByteArray>()

		for (message in messages) {
			val messageData = message.dump()

			result.add(messageData)
		}

		val totalSize = result.sumBy { it.size }

		val byteResult = ByteArray(4 + totalSize)
		println("Joda: ${totalSize}")

		var i = 0
		for (c in toChars(totalSize.toLong(), 4))
			byteResult[i++] = c

		for (packet in result)
			for (byte in packet)
				byteResult[i++] = byte

		return byteResult
	}
}

fun main(args: Array<String>) {
	val messagePackage = MessagePackage()
	val message = Message(MessageType.HI, MessageHiData())
	messagePackage.messages.add(message)

	for (c in messagePackage.dump())
		println("%h".format(c))

	//BackendComm()
}