package net.merayen.elastic.backend.context.action;

import java.io.File;
import java.util.List;

import net.merayen.elastic.backend.context.Action;
import net.merayen.elastic.util.NetListMessages;
import net.merayen.elastic.util.Postmaster;

/**
 * Loads a project. Automatically done when initiating the backend.
 */
public class LoadProjectAction extends Action {
	@Override
	protected void run() {
		if(!new File(env.project.path).isDirectory())
			throw new RuntimeException("Project not found. Should not happen as it should have been created or already exist");

		// TODO load messages from the netlist and send them to the backend
		//System.out.println(env.project.getNetList());

		System.out.println("Nettlæist: " + env.project.getNetList().getNodes().size());
		List<Postmaster.Message> messages = NetListMessages.disassemble(env.project.data.getRawNetList());

		backend_context.message_handler.sendToBackend(messages);
	}
}