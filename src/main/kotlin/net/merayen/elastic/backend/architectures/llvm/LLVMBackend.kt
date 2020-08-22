package net.merayen.elastic.backend.architectures.llvm

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

class LLVMBackend(code: String) {
	private val process: Process

	val inputStream: InputStream
	val outputStream: OutputStream
	val errorStream: InputStream

	init {
		val path = "/tmp/elastic-ccode.c"
		val outputPath = "$path.out"

		java.io.File(path).writeText(code)
		with(ProcessBuilder(listOf("clang-10", path, "-o", outputPath))) {
			redirectOutput(ProcessBuilder.Redirect.INHERIT)
			redirectError(ProcessBuilder.Redirect.INHERIT)
			start().waitFor()
		}

		process = with(ProcessBuilder(listOf(outputPath, outputPath))) {
			redirectOutput(ProcessBuilder.Redirect.PIPE)
			redirectError(ProcessBuilder.Redirect.PIPE)
			start()
		}

		// Wait for "HELLO"-message
		val hello = ByteArray(5)
		process.inputStream.read(hello)

		if (!hello.contentEquals(byteArrayOf('H'.toByte(),'E'.toByte(),'L'.toByte(),'L'.toByte(),'O'.toByte())))
			throw RuntimeException("Subprocess did not say hello to us in the manner we wanted it to: Got ${String(hello)}");

		inputStream = process.inputStream
		outputStream = process.outputStream
		errorStream = process.errorStream
	}

	private val thread = Thread {
		process.waitFor(500, TimeUnit.MILLISECONDS)
	}

	init {
		thread.name = "LLVMBackend"
	}

	fun stop() {
		thread.join()
	}
}