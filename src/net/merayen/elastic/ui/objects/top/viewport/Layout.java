package net.merayen.elastic.ui.objects.top.viewport;

import java.util.ArrayList;
import java.util.List;

public class Layout {
	private abstract class Item {
		float size; // width or height, depending on if this object is in a Horizontal() or Vertical(). Value can be 0 to 1
	}

	private class UserObject extends Item {
		private Object obj;

		private UserObject(Object obj) {
			this.obj = obj;
		}

		public Object getUserObject() {
			return obj;
		}
	}

	private class Ruler extends Item {
		private boolean vertical; // false = horizontal, true = vertical
		private List<Item> items = new ArrayList<>();

		private Ruler() {}

		public List<Item> getItems() {
			return new ArrayList<>(items);
		}
	}

	private class Translation {
		float x, y, width, height;

		public Translation(float x, float y, float width, float height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	/**
	 * Returned by getPlacements()
	 */
	static class CalculatedPosition {
		public float x, y, width, height;
		public Object obj;

		private CalculatedPosition(float x, float y, float width, float height, Object obj) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.obj = obj;
		}

		public String toString() {
			return String.format("CalculatedPosition(obj=%s, x=%f, y=%f, w=%f, h=%f)", obj, x, y, width, height);
		}
	}

	private final Ruler top;
	private final List<Item> list = new ArrayList<>(); // List of all the items, for easy access

	public Layout(Object main) {
		top = new Ruler();
		top.size = 1; // Always full size
		list.add(top);

		UserObject uobj = new UserObject(main);
		top.items.add(uobj);
		list.add(uobj);
	}

	/**
	 * Splits the obj in two vertically, adding another object to the left
	 */
	public void splitVertical(Object obj, Object new_obj) {
		if(hasUserObject(new_obj))
			throw new RuntimeException("Object is already added");

		UserObject userobj = getUserObject(obj);
		Ruler ruler = getRuler(userobj); // Get the ruler containing this object
		UserObject new_userobj = new UserObject(new_obj);

		if(ruler.vertical && ruler.items.size() == 1) { // Only 1 item? Make ruler horizontal first
			ruler.vertical = false;

		} else if(ruler.vertical) { // Ruler of this UserObject is not horizontal, we need to replace the UserObject with a horizontal ruler, inside the vertical one
			Ruler new_ruler = new Ruler();
			new_ruler.vertical = false;
			new_ruler.size = userobj.size;
			userobj.size = 1;

			ruler.items.set(ruler.items.indexOf(userobj), new_ruler);
			new_ruler.items.add(userobj);
			list.add(new_ruler);

			ruler = new_ruler;
		}

		ruler.items.add(ruler.items.indexOf(userobj) + 1, new_userobj);
		list.add(new_userobj);
	}

	/**
	 * Splits the obj in two horizontally, adding another object below it
	 */
	public void splitHorizontal(Object obj, Object new_obj) {
		if(hasUserObject(new_obj))
			throw new RuntimeException("Already has this object");

		UserObject userobj = getUserObject(obj);
		Ruler ruler = getRuler(userobj);
		UserObject new_userobj = new UserObject(new_obj);

		if(!ruler.vertical && ruler.items.size() == 1) { // Only 1 item? Make ruler vertical first
			ruler.vertical = true;

		} else if(!ruler.vertical) { // Ruler of this UserObject is not vertical, we need to replace the UserObject with a vertical ruler, inside the horizontal one
			Ruler new_ruler = new Ruler();
			new_ruler.vertical = true;
			new_ruler.size = userobj.size;
			userobj.size = 1;

			ruler.items.set(ruler.items.indexOf(userobj), new_ruler);
			new_ruler.items.add(userobj);
			list.add(new_ruler);

			ruler = new_ruler;
		}

		ruler.items.add(ruler.items.indexOf(userobj) + 1, new_userobj);
		list.add(new_userobj);
	}

	/**
	 * Resizes @param obj and the object after it.
	 */
	private void resize(Item item, float size) {
		if(size < 0 || size > 1)
			throw new RuntimeException("Size must be from 0 to 1");

		Ruler ruler = getRuler(item);

		if(ruler.items.indexOf(item) == ruler.items.size() - 1)
			return; // Rightmost/bottom-most objects can not be resized. Ignored.

		Item right_item = ruler.items.get(ruler.items.indexOf(item) + 1);

		normalize(ruler);

		float diff = size - item.size;

		// Limit resizable amount to the item right/below
		diff = Math.min(right_item.size, diff);

		right_item.size -= diff;
		item.size += diff;
	}

	public void resizeWidth(Object obj, float size) {
		UserObject userobj = getUserObject(obj);
		Ruler ruler = getRuler(userobj);

		if(ruler.vertical) { // Slider is vertical, need to resize the whole ruler instead, and not ourself only
			if(getRuler(ruler).vertical) // Is parent ruler also vertical? This is a critical error
				throw new RuntimeException("Should not happen");

			resize(ruler, size);
		} else { // Just resize our item
			resize(getUserObject(obj), size);
		}
	}

	public void resizeHeight(Object obj, float size) {
		UserObject userobj = getUserObject(obj);
		Ruler ruler = getRuler(userobj);

		if(!ruler.vertical) { // Slider is horizontal, need to resize the whole ruler instead, and not ourself only
			if(!getRuler(ruler).vertical) // Is parent ruler also horizontal? This is a critical error
				throw new RuntimeException("Should not happen");

			resize(ruler, size);
		} else { // Just resize our item
			resize(getUserObject(obj), size);
		}
	}

	public void remove(Object obj) {
		
	}

	public void updateLayout(float width, float height) {
		
	}

	/**
	 * Retrieves a list of all your objects with position and dimension.
	 */
	public List<CalculatedPosition> getLayout() {
		List<CalculatedPosition> result = new ArrayList<>();

		handle(result, top, new Translation(0, 0, 1, 1));

		return result;
	}

	private void handle(List<CalculatedPosition> result, Ruler ruler, Translation t) {
		normalize(ruler); // Make the result a sum of 1
		Translation new_t = new Translation(t.x, t.y, t.width, t.height);

		if(ruler.vertical) {
			for(Item item : ruler.items) {
				if(item instanceof UserObject)
					result.add(new CalculatedPosition(new_t.x, new_t.y, new_t.width, new_t.height * item.size, ((UserObject)item).obj));
				else // is Ruler()-object
					handle(result, (Ruler)item, new Translation(new_t.x, new_t.y, new_t.width, new_t.height * item.size));

				new_t.y += t.height * item.size;
			}

		} else { // Horizontal
			for(Item item : ruler.items) {
				if(item instanceof UserObject)
					result.add(new CalculatedPosition(new_t.x, new_t.y, new_t.width * item.size, new_t.height, ((UserObject)item).obj));
				else // is Ruler()-object
					handle(result, (Ruler)item, new Translation(new_t.x, new_t.y, new_t.width * item.size, new_t.height));

				new_t.x += t.width * item.size;
			}
		}
	}

	/*private void transform(CalculatedPosition to_transform, float x, float y, float width, float height) {
		to_transform.width *= width;
		to_transform.height *= height;
		to_transform.x += x;
		to_transform.y += y;
	}*/

	private void normalize(Ruler ruler) {
		float sum = 0;

		//for(Item item : ruler.items)
		//	if(item.size <= 0)
		//		throw new RuntimeException("Invisible layout item, should be removed before getting invisible");

		for(Item item : ruler.items)
			sum += item.size;

		if(sum > 0)
			for(Item item : ruler.items)
				item.size *= (1/sum);
		else
			for(Item item : ruler.items)
				item.size = 1f / ruler.items.size();
	}

	public Ruler getRuler(Item item) {
		for(Item x : list)
			if(x instanceof Ruler && ((Ruler)x).items.contains(item))
				return (Ruler)x;

		throw new RuntimeException("Should not happen");
	}

	private boolean hasUserObject(Object obj) {
		for(Item item : list)
			if(item instanceof UserObject && ((UserObject)item).obj == obj)
				return true;

		return false;
	}

	private UserObject getUserObject(Object obj) {
		for(Item item : list)
			if(item instanceof UserObject && ((UserObject)item).obj == obj)
				return (UserObject)item;

		throw new RuntimeException("User object not in the layout already");
	}
}