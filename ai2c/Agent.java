package ai2c;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Vector;
import javax.imageio.ImageIO;

class Agent {
	boolean clicked = false;
	public Vector<int[]> path = new Vector<int[]>(); //elem 0 is goal. path.get(path.size()-1) is init pos
	int[] bigGoal = new int[] {100, 100};
	Planner.PathAndFrontier combo;
	PriorityQueue<State> frontier = new PriorityQueue<>();
	Image bigGoalGrn, atBigGoalNeonGrn, curPosPur;

	void drawPlan(Graphics g, Model m) {
		drawDots(g, m);
		drawFrontier(g);
		drawPath(g, m);
		
		if(path.size() > 0 && m.getX() == m.getDestX() && m.getY() == m.getDestY())
				m.setDest(path.get(path.size()-1)[0], path.get(path.size()-1)[1]);
//		else if(path.size() == 1)
//			m.setDest(path.get(path.size()-1)[0], path.get(path.size()-1)[1]);

	}

	void update(Model m) throws IOException {
		Controller c = m.getController();
		combo = (new Planner(m, bigGoal[0], bigGoal[1])).ucs();
		path = combo.path;
		frontier = combo.frontier;
		while(true) {
			MouseEvent e = c.nextMouseEvent();
			if(e == null)
				break;
			
			bigGoal = new int[] {e.getX(), e.getY()};
//			combo = (new Planner(m, bigGoal[0], bigGoal[1])).ucs();
//			path = combo.path;
//			frontier = combo.frontier;
			
		}
	}
	
	void drawDots(Graphics g, Model m) {
		g.drawImage(bigGoalGrn, bigGoal[0]-3, (int)bigGoal[1] -3, null);
		g.drawImage(curPosPur, (int)m.getX() - 3, (int)m.getY() - 3, null);
		if(m.getX() == m.getDestX() && m.getY() == m.getDestY())
			g.drawImage(atBigGoalNeonGrn, (int)m.getDestX()-3, (int)m.getDestY() -3, null);
	}
	
	void drawPath(Graphics g, Model m) {
			g.setColor(Color.white);
			int[] last = bigGoal;
//			System.out.println("last: " + last[0] + "," + last[1]);
			for(int i = 0; i < path.size(); i++) {
//				System.out.println("i:" + i + "  drawing from: " + last[0] + "," + last[1] + "  to " + path.get(i)[0] + "," + path.get(i)[1]);
				g.drawLine(last[0], last[1], path.get(i)[0], path.get(i)[1]);
				
				last = new int[] {path.get(i)[0], path.get(i)[1]};
			}
//			System.out.println("ENDING last: " + last[0] + "," + last[1]);
			if(!path.isEmpty())
				g.drawLine((int)m.getX(), (int)m.getY(), path.get(path.size()-1)[0], path.get(path.size()-1)[1]);
	}
	
	void drawFrontier(Graphics g) {
		g.setColor(Color.YELLOW);
		for(State s : frontier)
			g.fillOval(s.pos[0], s.pos[1], 10, 10);
	}

	int roundTen(int s) {
		return ((s+5)/10)*10;
	}
	
	public static void main(String[] args) throws Exception {
		Controller.playGame();
	}
	
	Agent() throws IOException {
		bigGoalGrn = ImageIO.read(new File("grnDot.png"));
		curPosPur = ImageIO.read(new File("purDot.png"));
		atBigGoalNeonGrn = ImageIO.read(new File("neonGrnDot.png"));
	}

}