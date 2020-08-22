package net.merayen.elastic.backend.architectures.llvm

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer

internal class MessagePackageTest {
	@Test
	fun `message package dump`() {
		val data = MessagePackage(listOf(object : Message(10) {
			init {
				for (i in 0 until 10)
					data.put(i.toByte())
			}
		})).dump()

		assertEquals(18, data.size)

		// MessagePackage header
		assertEquals(0, data[0])
		assertEquals(0, data[1])
		assertEquals(0, data[2])
		assertEquals(14, data[3])

		// Message header
		assertEquals(0, data[4])
		assertEquals(0, data[5])
		assertEquals(0, data[6])
		assertEquals(10, data[7])

		// Message
		for (i in 0 until 10)
			assertEquals(i.toByte(), data[8 + i])
	}
}