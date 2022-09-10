package TerrainGeneration;

import java.awt.image.BufferedImage;

public class TerrainGeneration {
	for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {      
			double nx = x/width - 0.5, ny = y/height - 0.5;
			value[y][x] = noise(nx, ny);
		}
	}
	
	elevation[y][x] = noise(nx, ny);
	elevation[y][x] = noise(5.56 * nx, 5.56 * ny);
	elevation[y][x] = noise(x / wavelength, y / wavelength);
	elevation[y][x] =    1 * noise(1 * nx, 1 * ny) +  0.5 * noise(2 * nx, 2 * ny) + 0.25 * noise(4 * nx, 4 * ny);
	
	e  =    1 * noise(1 * nx, 1 * ny);
	+  0.5 * noise(2 * nx, 2 * ny);
	+ 0.25 * noise(4 * nx, 4 * ny);
	elevation[y][x] = e / (1 + 0.5 + 0.25);
	
	e =    1 * noise(1 * nx, 1 * ny);
	+  0.5 * noise(2 * nx, 2 * ny);
	+ 0.25 * noise(4 * nx, 4 * ny);
	e = e / (1 + 0.5 + 0.25);
	elevation[y][x] = Math.pow(e, 10.00);
}
