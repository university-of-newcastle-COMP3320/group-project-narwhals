package TerrainGeneration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.Random;

public class TerrainGeneration {
	static double lastX = 0;
	static double lastY = 0;
	static double lastZ = 0;
	public static void main(String[] args){
		Random seed = new Random();
		TerrainGeneration tg = new TerrainGeneration();
		Color[][] pixelSheet = new Color[1024][1024];

		pixelSheet = tg.drawMap(tg.generateMap(seed.nextInt(10, 20), 1024, 1024));

		String path1 = "C:/Users/Tom/IdeaProjects/group-project-narwhals/ProjectNarwhal/ProjectResources/TerrainTextures/" + "heightmap1" + ".png";
		String path2 = "C:/Users/Tom/IdeaProjects/group-project-narwhals/ProjectNarwhal/ProjectResources/TerrainTextures/" + "heightmap2" + ".png";
		String path3 = "C:/Users/Tom/IdeaProjects/group-project-narwhals/ProjectNarwhal/ProjectResources/TerrainTextures/" + "heightmap3" + ".png";
		String path4 = "C:/Users/Tom/IdeaProjects/group-project-narwhals/ProjectNarwhal/ProjectResources/TerrainTextures/" + "heightmap4" + ".png";

		BufferedImage[] imageForPngFile = new BufferedImage[4];
		for(int i = 0; i<4; i++){
			imageForPngFile[i] = new BufferedImage (512, 512, BufferedImage.TYPE_INT_RGB);
		}

		for(int z = 0; z < 4; z++){
			for (int x = 0; x < 512; x++) {
				for (int y = 0; y < 512; y++) {
					if(z == 0){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x][y].getRGB());
					}
					else if(z == 1){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x][y + 512].getRGB());
					}
					else if(z == 2){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 512][y].getRGB());
					}
					else {
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 512][y + 512].getRGB());
					}
				}
			}
		}

		File ImageFile1 = new File(path1);
		File ImageFile2 = new File(path2);
		File ImageFile3 = new File(path3);
		File ImageFile4 = new File(path4);
		try {
			ImageIO.write(imageForPngFile[0], "png", ImageFile1);
			ImageIO.write(imageForPngFile[1], "png", ImageFile2);
			ImageIO.write(imageForPngFile[2], "png", ImageFile3);
			ImageIO.write(imageForPngFile[3], "png", ImageFile4);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double[][] generateMap(int seed, int width, int height) {
		double[][] map = new double[width][height];
		double doubleSeed = seed;
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				map[x][y] = Math.abs(ImprovedNoise.noise(x*(doubleSeed)/width, y*(doubleSeed-5)/height, 0.0)); //make maths scale from 0 to 1
			}
		}
		return map;
	}

	public Color[][] drawMap(double[][] map) {
		Color pixelSheet[][] = new Color[1024][1024];
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				pixelSheet[x][y] = new Color((float) map[x][y], (float) map[x][y], (float) map[x][y]);
			}
		}
		return pixelSheet;
	}
}
