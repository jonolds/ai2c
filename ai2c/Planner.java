package ai2c;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.Vector;

class Planner extends Agent{
	Model m;
	State start, goal;
	final int[] act = new int[] {10, -10, 10, 0, 10, 10, 0, 10, 0, -10, -10, -10, -10, 0, -10, 10};
	
	Planner(Model m, int destX, int destY) throws IOException {
		this.m = m;
		this.start = new State((int)m.getX(), (int)m.getY());
		this.goal = new State(roundTen(destX), roundTen(destY));
	}

	PathAndFrontier ucs() {
		PriorityQueue<State> frontier = new PriorityQueue<State>(new CostComp()) {{add(start);}};
		TreeSet<State> visited = new TreeSet<State>(new PosComp()) {{add(start);}};
		State best = null;

		while(!frontier.isEmpty()) {
			State s = frontier.poll();
			if(new PosComp().compare(s, goal) == 0) if(best == null || best.cost > s.cost) {
				best = s;
				break;
			}
					
			for(int i = 0; i < 16; i+=2) {
				int[] newPos = new int[] {s.pos[0] + act[i], s.pos[1] + act[i+1]};
				if(newPos[0] >= 0 && newPos[0] < 1200 && newPos[1] >= 0 && newPos[1] < 600) {
					float speed = m.getTravelSpeed((float)(s.pos[0] + newPos[0])/2, (float)(s.pos[1] + newPos[1])/2);
					double actCost = this.getDistance(s.pos[0], s.pos[1], newPos[0], newPos[1])/speed;
					State newChild = new State(actCost, s, newPos[0], newPos[1]);
					if(visited.contains(newChild)) {
						State oldChild = visited.floor(newChild);
						if(oldChild != null) if (s.cost + actCost < oldChild.cost) {
							oldChild.cost = s.cost + actCost;
							oldChild.parent = s;
						}
					}
					else {
						newChild.cost = s.cost + actCost;
						frontier.add(newChild);
						visited.add(newChild);
					}
				}
			}
		}
		return new PathAndFrontier(state2moves(best), frontier);
	}

	public Vector<int[]> state2moves(State s) {
		Vector<int[]> moves = new Vector<>();
		if(s != null)
			while(s.parent != null) {
				moves.add(new int[]{s.pos[0], s.pos[1]});
				s = s.parent;
			}
//		for(int[] i : moves)
//		System.out.println(i[0] + "," + i[1]);
		return moves;
	}
	
	public class PathAndFrontier {
		Vector<int[]> path;
		PriorityQueue<State> frontier;
		PathAndFrontier(Vector<int[]> path, PriorityQueue<State> frontier) {
			this.path = path; this.frontier = frontier;
		}
	}
	
	public float getDistance(int x1, int y1, int x2, int y2) {
		return (float)Math.sqrt((float)Math.pow(x2 - x1, 2) + (float)Math.pow(y2-y1, 2));
	}
	
	class CostComp implements Comparator<State> {
		public int compare(State a, State b) {
			return Double.compare(a.cost, b.cost);
		}
	}
	
	class PosComp implements Comparator<State> {
		public int compare(State a, State b) {
			for(int i = 0; i < 2; i++) {
				if(a.pos[i] < b.pos[i])
					return -1;
				else if(a.pos[i] > b.pos[i])
					return 1;
			}
			return 0;
		}
	}

	void print(int[] s) {
		System.out.println("[" + s[0] + "," + s[1] + "]");
	}
}