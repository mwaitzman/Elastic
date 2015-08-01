package net.merayen.merasynth.netlist.datapacket;

public class AudioRequest extends DataPacket {
	public long sample_offset; // Increasing sample counter. 
	public int sample_count; // How many samples requested
	public int sample_rate; // Sample rate to respond in

	public int getSize() {
		return 8 + 4 + 4;
	}
}