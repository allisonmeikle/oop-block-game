package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		int score = 0;
		Color[][] c = board.flatten();

		for (int i = 0; i<c.length; i++){
			for (int j = 0; j<c.length; j++){
				if (c[i][j] == targetGoal && this.undiscoveredBlobSize(i,j,c,new boolean[c.length][c.length]) > score){
					score = this.undiscoveredBlobSize(i,j,c,new boolean[c.length][c.length]);
				}
			}
		}
		return score;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}

	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		if (unitCells[i][j] != targetGoal){
			return 0;
		}
		if (visited[i][j]){
			return 0;
		}
		visited[i][j] = true;

		if (i == 0 && j == 0){
			return 1 + undiscoveredBlobSize(i,j+1,unitCells,visited) + undiscoveredBlobSize(i+1,j,unitCells,visited);
		}
		if (j == visited.length-1 && i == 0){
			return 1 + undiscoveredBlobSize(i+1,j,unitCells,visited) + undiscoveredBlobSize(i,j-1,unitCells,visited);
		}
		if (j == 0 && i == visited.length-1){
			return 1 + undiscoveredBlobSize(i,j+1,unitCells,visited) + undiscoveredBlobSize(i-1,j,unitCells,visited);
		}
		if (i == visited.length-1 && j == visited.length-1){
			return 1 + undiscoveredBlobSize(i,j-1,unitCells,visited) + undiscoveredBlobSize(i-1,j,unitCells,visited);
		}
		if (j == 0){
			return 1 + undiscoveredBlobSize(i,j+1,unitCells,visited) + undiscoveredBlobSize(i+1,j,unitCells,visited) + undiscoveredBlobSize(i-1,j,unitCells,visited);
		}
		if (i == 0){
			return 1 + undiscoveredBlobSize(i,j+1,unitCells,visited) + undiscoveredBlobSize(i,j-1,unitCells,visited) + undiscoveredBlobSize(i+1,j,unitCells,visited);
		}
		if (i == visited.length-1){
			return 1 + undiscoveredBlobSize(i,j+1,unitCells,visited) + undiscoveredBlobSize(i-1,j,unitCells,visited) + undiscoveredBlobSize(i,j-1,unitCells,visited);
		}
		if (j == visited.length-1){
			return 1 + undiscoveredBlobSize(i+1,j,unitCells,visited) + undiscoveredBlobSize(i-1,j,unitCells,visited) + undiscoveredBlobSize(i,j-1,unitCells,visited);
		}
		return 1 + undiscoveredBlobSize(i-1,j,unitCells,visited) + undiscoveredBlobSize(i,j-1,unitCells,visited) + undiscoveredBlobSize(i,j+1,unitCells,visited) + undiscoveredBlobSize(i+1,j,unitCells,visited);
	}
}
