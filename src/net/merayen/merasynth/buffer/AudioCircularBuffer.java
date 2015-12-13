package net.merayen.merasynth.buffer;

import java.util.ArrayList;
import java.util.List;

/* 
 * Multi-channel audio buffer.
 * TODO Cut down on channels if any dead channels are present. Like, if no data has been written on a channel, kill it after a while
 * Not thread safe
 *
 * There is one weakness here, and that is if there is being written/read more on some channels than other channels.
 * This might make some channels overflow (loose audio data) or output silence. Reader and writer is responsible for
 * synchronize writing to all channels.
 * Call verify() after writing to explicitly check this.
 *
 * TODO Implement dropChannel() to cut away no more used channels.
 * Dead channels (not used anymore, like a voice) should of course not write any more.
 *
 */
public class AudioCircularBuffer {
	public class OutOfSyncException extends RuntimeException {
		/* One or more channels are out of sync with eachother */
	}

	private final int MAX_CHANNELS = 32;

	private long write_position;
	private long read_position;
	private final FloatCircularBuffer[] buffers = new FloatCircularBuffer[MAX_CHANNELS]; // TODO Make this a dynamic List instead, allowing many channels
	private final int buffer_size; // Size in samples for each channel
	private boolean is_engaged;

	public AudioCircularBuffer(int buffer_size) {
		this.buffer_size = buffer_size;
	}

	public void write(int channel, int start, int stop, float[] data) {
		ensure(channel);
		buffers[channel].write(data, start, stop);
	}

	/*
	 * Writes all the available data from the source buffer.
	 */
	public void writeFromBuffer(AudioCircularBuffer source) {
		List<Short> channels = source.getChannels();
		float[] samples = new float[source.available()];
		for(short channel : channels) {
			source.read(channel, samples);
			this.write(channel, 0, samples.length, samples);
		}
	}

	/*
	 * Copy data from us to the destination float array.
	 * Returns the floats actually copied.
	 */
	public int read(int channel, float[] destination) {
		if(channel >= MAX_CHANNELS)
			throw new RuntimeException(String.format("Channel No. %i does not exist. Only got %i channels", channel, MAX_CHANNELS));

		if(buffers[channel] == null) {// Channel not active
			for(int i = 0; i < destination.length; i++)
				destination[i] = 0;

			return 0;
		}

		return buffers[channel].read(destination);
	}

	/*
	 * Read only a part of this buffer
	 */
	public int read(int channel, int dest_start, int length, float[] destination) {
		return buffers[channel].read(dest_start, length, destination);
	}

	private void ensure(int channel) { // TODO remove dead channels automatically
		if(channel >= MAX_CHANNELS)
			throw new RuntimeException(String.format("Can't create channel No. %i. Max %i channels allowed", channel, MAX_CHANNELS));

		if(buffers[channel] == null)
			buffers[channel] = new FloatCircularBuffer(buffer_size);
	}

	/*
	 * Returns a list of active channels.
	 */
	public ArrayList<Short> getChannels() {
		ArrayList<Short> channels = new ArrayList<>();

		for(short i = 0; i < buffers.length; i++)
			if(buffers[i] != null)
				channels.add(i);

		return channels;
	}

	/*
	 * Returns count of available samples.
	 */
	public int available() {
		int result = 0;
		for(FloatCircularBuffer fcb : buffers)
			if(fcb != null)
				result = Math.max(result, fcb.available());

		return result;
	}

	public int getSize() {
		return buffer_size;
	}

	public void engage() {
		if(is_engaged)
			throw new RuntimeException("Unexpected call to engage() as we are already engaged. Did you forget to call disengage() somewhere?");

		is_engaged = true;
	}

	/*
	 * Checks that all the channels are in sync, that no channel contains more/less than other channels.
	 * Call this when you are done writing to this buffer.
	 */
	public void disengage() {
		if(!is_engaged)
			throw new RuntimeException("Unexpected call to disengage(). Have you forgotten to call engage()?");

		int available = Integer.MIN_VALUE;
		for(int i = 0; i < buffers.length; i++)
			if(buffers[i] != null)
				if(available == Integer.MIN_VALUE)
					available = buffers[i].available();
				else if(available != buffers[i].available())
					throw new OutOfSyncException();

		is_engaged = false;
	}
}
