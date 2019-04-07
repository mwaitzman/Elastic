package net.merayen.elastic.ui;

public class TranslationData {
	public float x = 0, y = 0; // Object's origin (relative to parent)
	public float scaleX = 1, scaleY = 1; // Scales the content INSIDE, not ourself
	public Rect clip;

	public TranslationData() {

	}

	public TranslationData(TranslationData t) {
		x = t.x;
		y = t.y;
		scaleX = t.scaleX;
		scaleY = t.scaleY;
	}

	public void translate(TranslationData td) {
		x = x + td.x / scaleX;
		y = y + td.y / scaleY;

		if(td.clip != null) {
			if(clip == null)
				clip = new Rect(
					x + td.clip.x1 / scaleX,
					y + td.clip.y1 / scaleY,
					x + td.clip.x2 / scaleX,
					y + td.clip.y2 / scaleY
				);
			else
				clip.clip(
					x + td.clip.x1 / scaleX,
					y + td.clip.y1 / scaleY,
					x + td.clip.x2 / scaleX,
					y + td.clip.y2 / scaleY
				);
		}

		scaleX *= td.scaleX;
		scaleY *= td.scaleY;
	}

	public String toString() {
		return String.format(
			"X=%f, Y=%f, scaleX=%f, scaleY=%f, clip=%s",
			x, y, scaleX, scaleY, clip
		);
	}
}