package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] brd = board.flatten();
		int tally = 0;
		System.out.println(GameColors.colorToString(targetGoal));

		Color[] top = brd[0];
		Color[] bottom = brd[brd.length-1];
		Color[] lhs = new Color[brd.length-2];
		Color[] rhs = new Color[brd.length-2];

		for (int i = 1; i<brd.length-1; i++){
			lhs[i-1] = brd[i][0];
			rhs[i-1] = brd[i][brd.length-1];
		}

		if (top[0] == targetGoal){
			tally += 1;
		}
		if (top[top.length-1] == targetGoal){
			tally += 1;
		}
		if (bottom[0] == targetGoal){
			tally += 1;
		}
		if (bottom[bottom.length-1] == targetGoal){
			tally += 1;
		}

		for (Color c : top){
			if (c == targetGoal){
				tally += 1;
			}
		}
		for (Color c : bottom){
			if (c == targetGoal){
				tally += 1;
			}
		}
		for (Color c : lhs){
			if (c == targetGoal){
				tally += 1;
			}
		}
		for (Color c : rhs){
			if (c == targetGoal){
				tally += 1;
			}
		}
		return tally;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
