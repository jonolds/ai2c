package ai2c;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.Vector;

class Agent {
	boolean newUcsFlag = false, clicked = false;
	public Vector<Integer[]> path = new Vector<Integer[]>();
	int[] bigGoal = new int[] {100, 100};
	Planner plan;
	TreeSet<State> visited;
	PriorityQueue<State> frontier = new PriorityQueue<>();
	


	void drawPlan(Graphics g, Model m) {
		g.setColor(Color.red);
		//g.drawLine((int)m.getX(), (int)m.getY(), (int)m.getDestX(), (int)m.getDestY());
		
		g.fillOval(95, 95, 10, 10);
		
		//System.out.println(m.getX() + "," + m.getY() + "      " + m.getDestX() + "," + m.getDestY());
		
		g.setColor(Color.YELLOW);
		for(State s : frontier)
			g.fillOval(s.pos[0], s.pos[1], 10, 10);
		if(newUcsFlag)
			if(path.size() > 0) {
				g.setColor(Color.white);
//				Integer[] last = new Integer[] {(int)m.getX(), (int)m.getY()};
				Integer[] last = new Integer[] {(int)m.getDestX(), (int)m.getDestY()};
				for(int i = 0; i < path.size()-1; i++) {
					//System.out.println(path.get(i)[0] + "," + path.get(i)[1]);
					if(last == null)
						last = new Integer[] {(int)m.getDestX(), (int)m.getDestY()};
					g.drawLine(last[0], last[1], path.get(i)[0], path.get(i)[1]);
					last = new Integer[] {path.get(i)[0], path.get(i)[1]};
	//				int x1 = path.get(i)[0], y1 = path.get(i)[1];
	//				int x2 = path.get(i+1)[0], y2 = path.get(i+1)[1];
	//				System.out.println(x1 + "," + y1 + " " + x2 + "," + y2);
	//				g.drawLine(x1, y1, x2, y2);
				}
			}
		
		
		
//		if(newUcsFlag) {
//			System.out.println(frontier.size());
//			g.setColor(Color.yellow);
//			
//			g.setColor(Color.RED);
//			for(Integer[] iArr: path)
//				g.fillOval(iArr[0], iArr[1], 4, 4);
//			drawPath(g, m);
//			newUcsFlag = false;
//		}
	}

	public void drawPath(Graphics g, Model m) {
		g.setColor(Color.green);
		for(int i = 0; i < path.size()-1; i++) {
			int x1 = path.get(i)[0], y1 = path.get(i)[1];
			int x2 = path.get(i+1)[0], y2 = path.get(i+1)[1];
			//System.out.println(x1 + "," + y1 + " " + x2 + "," + y2);
			g.drawLine(x1, y1, x2, y2);
		}
	}

	void update(Model m) {
		Controller c = m.getController();
		while(true) {
			MouseEvent e = c.nextMouseEvent();
			if(e == null)
				break;
			bigGoal = new int[] {e.getX(), e.getY()};
			plan = new Planner(m, e.getX(), e.getY());
			path = plan.ucs();
			visited = plan.visited;
			frontier = plan.frontier;
			if(!path.isEmpty())
				newUcsFlag = true;
			else
				System.out.println("returned path empty");
			//m.setDest(e.getX(), e.getY());

		}
	}

	int roundTen(int s) {
		return ((s+5)/10)*10;
	}
	
	public static void main(String[] args) throws Exception {
		Controller.playGame();
	}

}