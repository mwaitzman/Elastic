package net.merayen.elastic.backend.architectures.local.lets;

import java.util.ArrayList;
import java.util.List;

import net.merayen.elastic.backend.architectures.local.LocalProcessor;
import net.merayen.elastic.backend.logicnodes.Format;

public abstract class Outlet extends Portlet {
	public int written; // Number of samples written yet. Readers must respect this
	public final List<LocalProcessor> connected_processors = new ArrayList<>();
	public final int buffer_size;

	public Outlet(int buffer_size) {
		this.buffer_size = buffer_size;
	}

	@Override
	public void reset(int sample_offset) {
		written = sample_offset;
	}

	/**
	 * Notifies receiving ports about new data.
	 */
	public void push() {
		if(written > buffer_size)
			throw new RuntimeException("LocalProcessor has written too much into the Outlet");

		for(LocalProcessor lp : connected_processors)
			lp.schedule();
	}

	public boolean satisfied() {
		return written == buffer_size;
	}

	public abstract Format getFormat();

	public abstract Class<? extends Inlet> getInletClass();

	public abstract void forwardFromOutlet(Outlet source);
}