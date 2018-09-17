package ai2c;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.Vector;

class Agent {
	boolean newUcsFlag = false, clicked = false;
	public Vector<int[]> path = new Vector<int[]>();
	int[] bigGoal = new int[] {100, 100};
	Planner plan;
	TreeSet<State> visited;
	PriorityQueue<State> frontier = new PriorityQueue<>();
	


	void drawPlan(Graphics g, Model m) {
		g.setColor(Color.pink);
		g.fillOval(97, 97, 6, 6);
		
		//draw frontier
		g.setColor(Color.YELLOW);
		for(State s : frontier)
			g.fillOval(s.pos[0], s.pos[1], 10, 10);
		
		//draw path
		if(!path.isEmpty()) {
			g.setColor(Color.white);
			int[] last = bigGoal;
			for(int i = 0; i < path.size(); i++) {
				if(last == null)
					last = new int[] {(int)m.getDestX(), (int)m.getDestY()};
				g.drawLine(last[0], last[1], path.get(i)[0], path.get(i)[1]);
				last = new int[] {path.get(i)[0], path.get(i)[1]};
			}

			path.removeElementAt(path.size()-1);
		}
		
		
		if(path.size() > 1)
			m.setDest(path.get(path.size()-2)[0], path.get(path.size()-2)[1]);
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
			bigGoal = new int[] {e.getX(), e.getY()};

		}
	}

	int roundTen(int s) {
		return ((s+5)/10)*10;
	}
	
	public static void main(String[] args) throws Exception {
		Controller.playGame();
	}

}