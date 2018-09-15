package original;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.Timer;

class AgentORIG {

	public static void main(String[] args) throws Exception {
		ControllerORIG c = new ControllerORIG();
		c.view = new ViewORIG(c, c.model); //creates JFrame -> spawns thread to pump events/keep program running
		new Timer(20, c.view).start(); //ActionEvent at regular intervals; handled by View.actionPerformed
	}
	
	void drawPlan(Graphics g, ModelORIG m) {
		g.setColor(Color.red);
		g.drawLine((int)m.getX(), (int)m.getY(), (int)m.getDestX(), (int)m.getDestY());
	}
	
	void update(ModelORIG m) {
		ControllerORIG c = m.getController();
		while(true) {
			MouseEvent e = c.nextMouseEvent();
			if(e == null)
				break;
			m.setDest(e.getX(), e.getY());
		}
	}
		
	int roundTen(int s) {
		return ((s+5)/10)*10;
	}
}