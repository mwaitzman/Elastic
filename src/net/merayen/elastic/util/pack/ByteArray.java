package net.merayen.elastic.util.pack;

public class ByteArray extends PackType {
	public byte[] data;

	public ByteArray(byte[] in) {
		data = in;
	}

	@Override
	public byte[] onDump() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte getIdentifier() {
		// TODO Auto-generated method stub
		return 0;
	}
}