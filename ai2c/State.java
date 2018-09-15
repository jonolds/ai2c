package ai2c;

class State {
	double cost;
	State parent;
	int[] pos;
	
	State(int x, int y) {
		pos = new int[]{x, y};
	}
	State(double cost, State parent) {
		this.cost = cost;
		this.parent = parent;
	}
	State(double cost, State parent, int x, int y) {
		this(cost, parent);
		pos = new int[]{x, y};
	}

	State(State _parent, int[] pos) {
		this(0.0, _parent, pos[0], pos[1]);
	}

	void printAll() {
		System.out.println("[" + pos[0] + "," + pos[1] + "]  cost: " + cost + "  parent: " + ((parent == null) ? "null" : parent));
	}
	void printPos() {
		System.out.println("[" + pos[0] + "," + pos[1] + "]");
	}
	void printCost() {
		System.out.println(cost);
	}
}