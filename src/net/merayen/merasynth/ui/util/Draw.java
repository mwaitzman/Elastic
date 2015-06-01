package net.merayen.merasynth.ui.util;

import net.merayen.merasynth.ui.objects.UIObject;

public class Draw {
	/*
	 * Helper class to make drawing easy inside the UIObject()s.
	 * UIObjects uses this.
	 * This class mostly translates our internal floating point coordinate system to pixels,
	 * making it easier to draw stuff.
	 */
	
	private java.awt.Graphics2D g2d;
	private UIObject uiobject;
	
	public Draw(UIObject obj, java.awt.Graphics2D g) {
		uiobject = obj;
		g2d = g;
	}
	
	public void fillRect(float x, float y, float width, float height) {
		java.awt.Point point = uiobject.getAbsolutePixelPoint(x, y);
		java.awt.Dimension dimension = uiobject.getPixelDimension(width, height);
		g2d.fillRect(point.x, point.y, dimension.width, dimension.height);
	}
	
	public void setStroke(float width) {
		g2d.setStroke(new java.awt.BasicStroke(uiobject.convertUnitToPixel(width)));
	}
	
	public void line(float x1, float y1, float x2, float y2) {
		java.awt.Point point1 = uiobject.getAbsolutePixelPoint(x1, y1);
		java.awt.Point point2 = uiobject.getAbsolutePixelPoint(x2, y2);
		g2d.drawLine(point1.x, point1.y, point2.x, point2.y);
	}
	
	public void fillOval(float x, float y, float width, float height) {
		java.awt.Point point = uiobject.getAbsolutePixelPoint(x, y);
		java.awt.Dimension dimension = uiobject.getPixelDimension(width, height);
		g2d.fillOval(point.x, point.y, dimension.width, dimension.height);
	}
}
