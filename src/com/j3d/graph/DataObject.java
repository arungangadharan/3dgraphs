package com.j3d.graph;

import java.util.HashMap;
import java.util.Map;

public class DataObject {

	private double xVal;
	private double yVal;
	private double zVal;
	private  Map <String, String> values = new HashMap<String, String> ();
	
	DataObject(double xVal, double yVal, double zVal){
		
		this.xVal = xVal;
		this.yVal = yVal;
		this.zVal = zVal;
	}
	
	public double getxVal() {
		return xVal;
	}
	public void setxVal(double xVal) {
		this.xVal = xVal;
	}
	public double getyVal() {
		return yVal;
	}
	public void setyVal(double yVal) {
		this.yVal = yVal;
	}
	public double getzVal() {
		return zVal;
	}
	public void setzVal(double zVal) {
		this.zVal = zVal;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
		
	
}
