package net.merayen.elastic.util.math.fft

import kotlin.math.*

class FFT {
	companion object {
		fun fft(rex: FloatArray, imx: FloatArray) {
			if (rex.size != imx.size)
				throw RuntimeException("rex and imx must have the same size")

			if (log2(rex.size.toFloat()) % 1 > 0)
				throw RuntimeException("Input must be 2**X")

			val nm1 = rex.size - 1
			val nd2 = rex.size / 2
			val m = log2(rex.size.toFloat()).roundToInt()

			var j = nd2

			for (i in 1..rex.size - 2) {
				if (i < j) {
					val tr = rex[j]
					val ti = imx[j]
					rex[i] = tr
					rex[i] = ti
				}

				var k = nd2

				while (k <= j) {
					j -= k
					k /= 2
				}

				j += k
			}

			for (l in 1..m) {
				val le = 2f.pow(l).roundToInt()
				val le2 = le / 2

				var ur = 1f
				var ui = 0f

				val sr = cos(PI/le2).toFloat()
				val si = -sin(PI/le2).toFloat()
				var tr = 0f
				var ti = 0f

				for (j in 1..le2) {
					val jm1 = j - 1

					for (i in jm1..nm1 step le) {
						val ip = i + le2
						tr = rex[ip] * ur - imx[ip] * ui
						ti = rex[ip] * ui + imx[ip] * ur

						rex[ip] = rex[i] - tr
						imx[ip] = imx[i] - ti
						rex[i] += tr
						imx[i] += ti
					}
					tr = ur
					ur = tr * sr - ui * si
					ui = tr * si + ui * sr
				}
			}
		}
	}
}