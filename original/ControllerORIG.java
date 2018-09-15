package original;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

class ControllerORIG implements MouseListener {
	AgentORIG agent;
	ModelORIG model; // holds all the game data
	ViewORIG view; // the GUI
	private LinkedList<MouseEvent> mouseEvents; // a queue of mouse events

	ControllerORIG() throws Exception {
		this.agent = new AgentORIG();
		this.mouseEvents = new LinkedList<MouseEvent>();
		this.model = new ModelORIG(this);
	}

	MouseEvent nextMouseEvent() {
		if(mouseEvents.size() == 0)
			return null;
		return mouseEvents.remove();
	}

	public void mousePressed(MouseEvent e) {
		if(e.getY() < 600) {
			mouseEvents.add(e);
			if(mouseEvents.size() > 20) // discard events if the queue gets big
				mouseEvents.remove();
		}
	}
	
	void update() {
		agent.update(model);
		model.update();
	}

	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }
}