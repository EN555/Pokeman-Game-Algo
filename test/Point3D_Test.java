import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import api.geo_location;
import gameClient.*;
import gameClient.util.Point3D;

public class Point3D_Test {

	@BeforeAll
	static void runOnceBeforeClass() {
		System.out.println("Graph_Tests");
	}

	@AfterAll
	static void runOnceAfterAll() {
		System.out.println("The Test Finished!");
	}

	@BeforeEach
	void runBeforeEach() {
		System.out.println("The Next Test");
	}

	@AfterEach
	void runAfterEach() {
		System.out.println("The  Current Test Finished!");
	}
	
	public boolean helperEquals(geo_location a , geo_location b) {
		if(a.x() == b.x() && a.y() == b.y() && a.z() == b.z())	
				return true;
		return false;
	}
	
	@Test
	public void copyCheck() {
		
		geo_location  loc1 = new Point3D(1,2,3);
		geo_location loc2 = new Point3D(loc1);		
		assertTrue(helperEquals(loc1, loc2) , "the locations are equals");
		
		geo_location  loc3 = new Point3D(1.3452,2.2453,3.42534);
		geo_location loc4 = new Point3D(loc3);		
		assertTrue(helperEquals(loc3, loc4) , "the locations are equals");

		geo_location  loc5 = new Point3D(12341,23214132,13423);
		geo_location loc6 = new Point3D(loc5);		
		assertTrue(helperEquals(loc5, loc6) , "The locations are equals");
		
	}
	
	@Test
	public void DistanceCheck() {
		
		geo_location  loc1 = new Point3D(1,2,0);
		geo_location loc2 = new Point3D(loc1);		
		assertEquals(0.0, loc1.distance(loc2), "The distances are equals!");
		
		geo_location  loc3 = new Point3D(1.3452,2.2453,3.42534);
		geo_location loc4 = new Point3D(loc3);	
		assertEquals( loc3.distance(loc4), 0 , "The distances are equals!");
		
	}
	
	@Test
	public void readFromStringCheck() {
		
		geo_location  loc1 = new Point3D("1,2,3");
		geo_location loc2 = new Point3D(1,2,3);		
		assertTrue(helperEquals(loc1, loc2) , "the locations are equals");
		
		geo_location  loc3 = new Point3D("1.3452,2.2453,3.42534");
		geo_location loc4 = new Point3D(1.3452,2.2453,3.42534);		
		assertTrue(helperEquals(loc3, loc4) , "the locations are equals");

		geo_location  loc5 = new Point3D("12341,23214132,13423");
		geo_location loc6 = new Point3D(12341,23214132,13423);		
		assertTrue(helperEquals(loc5, loc6) , "The locations are equals");
		
	}
	

}
