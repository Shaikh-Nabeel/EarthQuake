package com.nabeel130.earthquake.models;

import java.util.List;

public class Response{
	private List<FeaturesItem> features;
	private Metadata metadata;
	private String type;

	public void setFeatures(List<FeaturesItem> features){
		this.features = features;
	}

	public List<FeaturesItem> getFeatures(){
		return features;
	}

	public void setMetadata(Metadata metadata){
		this.metadata = metadata;
	}

	public Metadata getMetadata(){
		return metadata;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}
}