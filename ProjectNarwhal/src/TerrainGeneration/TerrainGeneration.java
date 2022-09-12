package TerrainGeneration;

import java.awt.*;

public class TerrainGeneration {
	public static void main(String[] args){
		TerrainGeneration tg = new TerrainGeneration();
		tg.drawMap(tg.generateMap(808, 10, 10));
	}

	public double[][] generateMap(long seed, int width, int height) {
		double[][] map = new double[width][height];
		OpenSimplexNoise simplex = new OpenSimplexNoise(seed);

		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				map[x][y] = simplex.eval(x, y);
			}
		}
		return map;
	}

	public void drawMap(double[][] map) {
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				Color color = new Color((int) map[x][y], (int) map[x][y], (int) map[x][y]);
			}
		}
	}
}
