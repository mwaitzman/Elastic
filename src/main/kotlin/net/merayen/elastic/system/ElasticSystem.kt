package net.merayen.elastic.system

import net.merayen.elastic.system.actions.CreateDefaultProject
import net.merayen.elastic.system.intercom.CreateDefaultProjectMessage
import net.merayen.elastic.system.intercom.ElasticMessage
import net.merayen.elastic.util.tap.ObjectDistributor
import java.io.Closeable
import kotlin.math.min
import kotlin.reflect.KClass

/**
 * This class binds together the backend and the UI.
 * This is the top class for everything.
 *
 * Elastic has 3 modules:
 * 	- Backend
 * 	- UI
 * 	- DSP
 *
 * 	This class routes the messages correctly between them
 */
class ElasticSystem(
		projectPath: String,
		uiModule: KClass<out UIModule>,
		dspModule: KClass<out DSPModule>,
		backendModule: KClass<out BackendModule>
) : Closeable {
	val lock = Object()

	@Volatile
	private var handleMessages = false

	private var ui = uiModule.constructors.first().call()
	private var backend = backendModule.constructors.first().call(projectPath)
	private var dsp = dspModule.constructors.first().call()

	private val messagesFromUIDistributor = ObjectDistributor<ElasticMessage>()
	private val messagesFromBackendDistributor = ObjectDistributor<ElasticMessage>()
	private val messagesFromDSPDistributor = ObjectDistributor<ElasticMessage>()

	/**
	 * The thread that made us. We do not allow being used by another
	 */
	private val threadLock = Thread.currentThread().id


	init {
		val handler = object : ElasticModule.Handler {
			override fun onWakeUp() {
				synchronized(lock) {
					handleMessages = true
					lock.notifyAll()
					println("${System.currentTimeMillis() % 1000}: Notifisert!")
					//for (noe in Thread.currentThread().stackTrace.slice(3..4))
					//	println("\t${noe.className.split("net.merayen.elastic.")[1]} ${noe.methodName}")
				}
			}
		}

		ui.handler = handler
		dsp.handler = handler
		backend.handler = handler

		ui.start()
		dsp.start()
		backend.start()
	}


	/**
	 * Routes messages between the components.
	 */
	fun update(timeoutMilliseconds: Long) { // TODO perhaps don't do this, but rather trigger on events
		val start = System.currentTimeMillis() + timeoutMilliseconds
		assertCorrectThread()
		do {
			synchronized(lock) {
				if (handleMessages) {
					lock.wait(min(timeoutMilliseconds, 1000))
					if (handleMessages) {
						println("${System.currentTimeMillis() % 1000}: Notified, message status: ui=${ui.ingoing.size()}/${ui.outgoing.size()}, backend=${backend.ingoing.size()}/${backend.outgoing.size()}, dsp=${dsp.ingoing.size()}/${dsp.outgoing.size()}")
					} else {
						println("Woke up by myself")
					}
					handleMessages = false
				}
			}

			processMessagesFromBackend()
			processMessagesFromUI()
			processMessagesFromDSP()

			println("${System.currentTimeMillis() % 1000}: Done handling messages")

		} while (start > System.currentTimeMillis())
	}

	private fun processMessagesFromBackend() {
		for (message in backend.outgoing.receiveAll()) {
			// TODO soon: Filter messages
			ui.ingoing.send(message)
			dsp.ingoing.send(message)
			messagesFromBackendDistributor.push(message)
		}

		//if (!dsp.ingoing.isEmpty()) {
			synchronized(dsp.lock) {
				dsp.lock.notifyAll()
			}
		//}
	}

	private fun processMessagesFromUI() {
		for (message in ui.outgoing.receiveAll()) {
			backend.ingoing.send(message)
			messagesFromUIDistributor.push(message)
		}

		//if (!backend.ingoing.isEmpty()) {
			synchronized(backend.lock) {
				backend.lock.notifyAll()
			}
		//}
	}

	private fun processMessagesFromDSP() {
		for (message in dsp.outgoing.receiveAll()) {
			backend.ingoing.send(message)
			messagesFromDSPDistributor.push(message)
		}

		//if (!backend.ingoing.isEmpty()) {
			synchronized(backend.lock) {
				backend.lock.notifyAll()
			}
		//}
	}

	/**
	 * Send messages to Elastic from outside.
	 */
	@Synchronized
	fun send(message: ElasticMessage) {
		assertCorrectThread()

		println("ElasticSystem: $message")

		val backend = backend

		when (message) {
			is CreateDefaultProjectMessage -> {
				runAction(CreateDefaultProject(message))
			}
		}

		backend.ingoing.send(message)

		synchronized(lock) {
			lock.notifyAll()
		}
	}

	/**
	 * Only for debugging purposes.
	 * Listen to all messages being sent to the UI.
	 */
	fun listenToMessagesFromUI(func: (item: ElasticMessage) -> Unit) = messagesFromUIDistributor.createTap(func)

	/**
	 * Only for debugging purposes.
	 * Listen to all messages being sent to the backend.
	 */
	fun listenToMessagesFromBackend(func: (item: ElasticMessage) -> Unit) = messagesFromBackendDistributor.createTap(func)

	/**
	 * Only for debugging purposes.
	 * Listen to all messages being sent to the backend.
	 */
	fun listenToMessagesFromDSP(func: (item: ElasticMessage) -> Unit) = messagesFromDSPDistributor.createTap(func)

	private fun runAction(action: Action) {
		action.handler = object : Action.Handler {
			override fun onMessage(message: ElasticMessage) {
				backend.ingoing.send(message)
				synchronized(lock) {
					lock.notifyAll()
				}
			}
			override fun onUpdateSystem() = update(0)
		}

		// TODO soon: Should we close the tap? What happens here?
		val tap = listenToMessagesFromBackend { action.onMessageFromBackend(it) }.use {
			action.run()
		}
	}

	override fun close() {
		ui.close()
		dsp.close()
		backend.close()

		ui.join()
		dsp.join()
		backend.join()
	}

	private fun assertCorrectThread() {
		if (threadLock != Thread.currentThread().id)
			throw RuntimeException("ElasticSystem can only be used by the thread it was created in")
	}
}
