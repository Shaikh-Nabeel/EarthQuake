package com.nabeel130.earthquake.models;

public class Properties{

	private String code;
	private String type;
	private String title;
	private String magType;
	private Integer tsunami;
	private Double mag;
	private Integer gap;
	private Double rms;
	private String place;
	private String url;
	private Long time;
	private String detail;
	private String status;

	public Properties(String code, String type, String title, String magType, Integer tsunami, Double mag, Integer gap, String place, String url, Long time, String detail, String status) {
		this.code = code;
		this.type = type;
		this.title = title;
		this.magType = magType;
		this.tsunami = tsunami;
		this.mag = mag;
		this.gap = gap;
		this.place = place;
		this.url = url;
		this.time = time;
		this.detail = detail;
		this.status = status;
	}

	public Properties(){

	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setMagType(String magType){
		this.magType = magType;
	}

	public String getMagType(){
		return magType;
	}

	public void setTsunami(Integer tsunami){
		this.tsunami = tsunami;
	}

	public Integer getTsunami(){
		return tsunami;
	}

	public void setMag(Double mag){
		this.mag = mag;
	}

	public Double getMag(){
		return mag;
	}

	public void setGap(Integer gap){
		this.gap = gap;
	}

	public Integer getGap(){
		return gap;
	}

	public void setRms(Double rms){
		this.rms = rms;
	}

	public Double getRms(){
		return rms;
	}

	public void setPlace(String place){
		this.place = place;
	}

	public String getPlace(){
		return place;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setTime(Long time){
		this.time = time;
	}

	public Long getTime(){
		return time;
	}

	public void setDetail(String detail){
		this.detail = detail;
	}

	public String getDetail(){
		return detail;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

}
