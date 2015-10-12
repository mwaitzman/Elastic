package net.merayen.merasynth.ui.objects.top;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import net.merayen.merasynth.glue.nodes.GlueNode;
import net.merayen.merasynth.glue.nodes.GlueTop;
import net.merayen.merasynth.netlist.Supervisor;
import net.merayen.merasynth.ui.Point;
import net.merayen.merasynth.ui.event.IEvent;
import net.merayen.merasynth.ui.event.MouseWheelEvent;
import net.merayen.merasynth.ui.objects.UIGroup;
import net.merayen.merasynth.ui.objects.node.UINode;
import net.merayen.merasynth.ui.util.MouseHandler;

public class Top extends UIGroup {
	/*
	 * The topmost object, of 'em all
	 */
	public static abstract class Handler {
		public void onOpenProject(String file_path) {}
		public void onSaveProject() {}
		public void onSaveProjectAs() {}
		public void onClose() {}
	}
	private Handler handler;
	private MouseHandler mousehandler;
	private net.merayen.merasynth.glue.Context glue_context;

	// Scrolling, when dragging the background
	float start_scroll_x;
	float start_scroll_y;

	public Debug debug;
	private TopNodeContainer top_node_container = new TopNodeContainer();
	private TopMenuBar top_menu_bar;

	public Top(net.merayen.merasynth.glue.Context glue_context) {
		this.glue_context = glue_context;
	}

	protected void onInit() {
		add(top_node_container);

		// TODO Change scale by window size?
		translation.scale_x = 0.001f;
		translation.scale_y = 0.001f;

		top_node_container.translation.x = 0f;
		top_node_container.translation.y = 0f;
		top_node_container.translation.scale_x = 100f;
		top_node_container.translation.scale_y = 100f;

		mousehandler = new MouseHandler(this);
		mousehandler.setHandler(new MouseHandler.Handler() {
			@Override
			public void onMouseDrag(Point start_point, Point offset) {
				top_node_container.translation.x = (start_scroll_x + offset.x);
				top_node_container.translation.y = (start_scroll_y + offset.y);
			}

			@Override
			public void onMouseDown(Point position) {
				start_scroll_x = top_node_container.translation.x;
				start_scroll_y = top_node_container.translation.y;
			}
		});

		initDebug();
		initMenuBar();
	}

	private void initDebug() {
		debug = new Debug();
		debug.translation.y = 0.04f;
		debug.translation.scale_x = 100f;
		debug.translation.scale_y = 100f;
		add(debug);
		debug.set("DEBUG", "Has been enabled");
	}

	private void initMenuBar() {
		top_menu_bar = new TopMenuBar();
		top_menu_bar.translation.scale_x = 100f;
		top_menu_bar.translation.scale_y = 100f;
		add(top_menu_bar);

		top_menu_bar.setHandler(new TopMenuBar.Handler() {
			@Override
			public void onOpenProject(String path) {
				if(handler != null)
					handler.onOpenProject(path);
			}

			@Override
			public void onSaveProject() {
				if(handler != null)
					handler.onSaveProject();
			}

			@Override
			public void onSaveProjectAs() {
				if(handler != null)
					handler.onSaveProjectAs();
			}

			@Override
			public void onClose() {
				if(handler != null)
					handler.onClose();
			}
		});
	}

	protected void onDraw() {
		draw.setColor(50, 50, 50);
		draw.fillRect(0, 0, 1, 1); // XXX Ikke bruk draw_context, men meh

		debug.set("Top absolute_translation", absolute_translation);

		/*float t = (float)(Math.sin(System.currentTimeMillis() / 1000.0 * 2)) / 10f;
		top_node_container.translation.x = t + 500 / 1000f;
		top_node_container.translation.y = t + 500 / 1000f;*/

		super.onDraw();
	}

	protected void onEvent(IEvent event) {
		mousehandler.handle(event);

		if(event instanceof MouseWheelEvent) {
			MouseWheelEvent e = (MouseWheelEvent)event;

			float p_x = top_node_container.translation.scale_x;
			float p_y = top_node_container.translation.scale_y;

			if(e.getOffsetY() < 0) {
				top_node_container.translation.scale_x /= 1.1;
				top_node_container.translation.scale_y /= 1.1;
			}
			else if(e.getOffsetY() > 0) {
				top_node_container.translation.scale_x *= 1.1;
				top_node_container.translation.scale_y *= 1.1;
			} else {
				return;
			}

			top_node_container.translation.scale_x = Math.min(Math.max(top_node_container.translation.scale_x, .01f), 1000f);
			top_node_container.translation.scale_y = Math.min(Math.max(top_node_container.translation.scale_y, .01f), 1000f);

			float b_x = top_node_container.translation.x;
			float b_y = top_node_container.translation.y;
	
			float diff_x = (top_node_container.translation.scale_x - p_x);
			float diff_y = (top_node_container.translation.scale_y - p_y);
			float t = (float)(Math.sin(System.currentTimeMillis() / 1000));
			//top_node_container.translation.x += t;
			//top_node_container.translation.y += t;

			debug.set("Top scroll pos", String.format(
				"Before: %.2f,%.2f, TNC scale: %.2f,%.2f, scale diff: %.2f,%.2f",
				b_x,
				b_y,
				top_node_container.translation.scale_x,
				top_node_container.translation.scale_y,
				diff_x,
				diff_y
			));
		}
	}

	public UINode addNode(String class_path) {
		return top_node_container.addNode(class_path);
	}

	public ArrayList<UINode> getNodes() {
		return top_node_container.getNodes();
	}

	public UINode getNode(String id) {
		return top_node_container.getNode(id);
	}

	public GlueNode getGlueNode(UINode uinode) {
		/*
		 * Get the GlueNode that represents the uinode.
		 */
		return glue_context.glue_top.getNode(uinode.getID());
	}

	public Supervisor getSupervisor() {
		return glue_context.net_supervisor;
	}

	public GlueTop getGlueTop() {
		return glue_context.glue_top;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public JSONObject dump() {
		return top_node_container.dump();
	}

	public void restore(JSONObject obj) {
		top_node_container.restore(obj);
	}
}