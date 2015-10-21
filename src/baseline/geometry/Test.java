package baseline.geometry;

import java.util.ArrayList;
import java.util.List;

import baseline.htmlparser.WindowWallRatioParser;

public class Test {

	public static void main(String[] args) {
		Coordinate3D wall1 = new Coordinate3D();
		Coordinate3D wall2 = new Coordinate3D(0, 10, 0);
		Coordinate3D wall3 = new Coordinate3D(10, 10, 0);
		Coordinate3D wall4 = new Coordinate3D(10, 0, 0);
		
		Coordinate3D win1 = new Coordinate3D(1, 1, 0);
		Coordinate3D win2 = new Coordinate3D(1, 9, 0);
		Coordinate3D win3 = new Coordinate3D(9, 9, 0);
		Coordinate3D win4 = new Coordinate3D(9, 1, 0);
		
		List<Coordinate3D> wallCoords = new ArrayList<>();
		List<Coordinate3D> winCoords = new ArrayList<>();
		
		wallCoords.add(wall1);
		wallCoords.add(wall2);
		wallCoords.add(wall3);
		wallCoords.add(wall4);
		
		winCoords.add(win1);
		winCoords.add(win2);
		winCoords.add(win3);
		winCoords.add(win4);
		
		WindowWallRatioParser wwrp = new WindowWallRatioParser(winCoords, wallCoords);
		System.out.println("Before adjust ratio: "+wwrp.getWindowWallRatio());
		System.out.println("Before adjust wall area: "+wwrp.getWall().getArea());
		System.out.println("Before adjust window area: "+wwrp.getWindow().getArea());
		
		boolean isAdjusted = wwrp.adjustToThreshold();
		System.out.println("\nIs adjusted: "+isAdjusted+"\n");
		
		System.out.println("After adjust ratio: "+wwrp.getWindowWallRatio());
		System.out.println("After adjust wall area: "+wwrp.getWall().getArea());
		System.out.println("After adjust window area: "+wwrp.getWindow().getArea());
		
		List<Coordinate3D> adjustWinCoords = wwrp.getWindow().getCoords();
		for(Coordinate3D p : adjustWinCoords){
			System.out.println(p.getX()+", "+p.getY()+", "+p.getZ());
		}
	}

}
