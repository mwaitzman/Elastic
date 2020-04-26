package net.merayen.elastic.system

import net.merayen.elastic.system.intercom.ElasticMessage
import net.merayen.elastic.util.Postmaster
import java.io.Closeable

abstract class ElasticModule : Thread(), Closeable {
	interface Handler {
		/**
		 * Called when this module sends outgoing messages.
		 * ElasticSystem's thread will wake up and retrieve the message.
		 */
		fun onWakeUp()
	}

	var handler: Handler? = null

	val lock = Object()

	/**
	 * Messages sent to this module, that are to be read by this module.
	 */
	val ingoing = Postmaster<ElasticMessage>()

	/**
	 * Messages sent from this module, that are to be read by this module.
	 */
	val outgoing = Postmaster<ElasticMessage>()

	@Volatile
	var isRunning = false
		private set

	final override fun run() {
		isRunning = true
		mainLoop()
	}

	/**
	 * Implement the main code here. Like this:
	 *
	 * 	override fun mainLoop() {
	 *		// Your init code here
	 *
	 *		while (running) {
	 *			// Your mainloop here
	 *		}
	 *
	 *		// Your destructor code here
	 *	}
	 */
	abstract fun mainLoop()

	protected fun notifyElasticSystem() = handler!!.onWakeUp()

	override fun close() {
		isRunning = false
		join()
	}
}