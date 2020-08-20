package net.merayen.elastic.backend.architectures.llvm

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

class LLVMBackend(private val code: String) {
	private val process: Process

	var inputStream: InputStream
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