package com.nabeel130.earthquake.models;

import java.util.List;

public class Geometry{
	private List<Double> coordinates;
	private String type;

	public void setCoordinates(List<Double> coordinates){
		this.coordinates = coordinates;
	}

	public List<Double> getCoordinates(){
		return coordinates;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}
}