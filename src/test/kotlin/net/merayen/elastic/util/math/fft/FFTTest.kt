package net.merayen.elastic.util.math.fft

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.pow
import kotlin.math.roundToInt

internal class FFTTest {
	@Test
	fun testForwardAndBack() {
		for (i in 1..16) {
			val rexOriginal = DoubleArray(2f.pow(i).roundToInt())
			val imxOriginal = DoubleArray(rexOriginal.size)
			println("Testing with size ${rexOriginal.size}")

			for (i in rexOriginal.indices) {
				rexOriginal[i] = (Math.random() * 1000000).roundToInt() / 1000.0
				imxOriginal[i] = (Math.random() * 1000000).roundToInt() / 1000.0
			}

			val rex = rexOriginal.clone()
			val imx = imxOriginal.clone()

			FFT.fft(rex, imx)
//			for (i in rex.indices)
//				println("${(rex[i] * 100).roundToInt() / 100}, ${(imx[i] * 100).roundToInt() / 100}")

			FFT.ifft(rex, imx)
//			for (i in rex.indices)
//				println("${(rex[i] * 100).roundToInt() / 100}, ${(imx[i] * 100).roundToInt() / 100}")

			// Round away floating point errors
			for (i in rexOriginal.indices) {
				rex[i] = (rex[i] * 1000f).roundToInt() / 1000.0
				imx[i] = (imx[i] * 1000f).roundToInt() / 1000.0
			}

			assertArrayEquals(rexOriginal, rex)
			assertArrayEquals(imxOriginal, imx)
		}
	}
}