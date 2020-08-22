package net.merayen.elastic.backend.architectures.llvm

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer

internal class LLVMCommunicatorTest {
	@Test
	fun `spawn and communicate with process`() {
		val backend = LLVMBackend("""
			#include <stdio.h>
			#include <string.h>
			
			int main() {
				char buf[512];
				fgets(buf, sizeof buf, stdin);
				if (buf[strlen(buf)-1] == '\n')
					printf("The whole buffer\n");
				else
					printf("Truncated\n");

				return 0;
			}
		""".trimIndent())

		val com = LLVMCommunicator(backend, object : LLVMCommunicator.Handler {
			override fun onReceive(data: MessagePackage) {
				TODO()
			}
		})

		com.send(MessagePackage(listOf(
			object : Message(3) {
				init {
					for (i in 0 until 3)
						data.put(i.toByte())
				}
			}
		)))
	}
}