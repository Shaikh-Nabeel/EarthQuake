package com.nabeel130.earthquake.models;

public class Metadata{
	private Integer offset;
	private Long generated;
	private Integer limit;
	private Integer count;
	private String api;
	private String title;
	private String url;
	private Integer status;

	public void setOffset(Integer offset){
		this.offset = offset;
	}

	public Integer getOffset(){
		return offset;
	}

	public void setGenerated(Long generated){
		this.generated = generated;
	}

	public Long getGenerated(){
		return generated;
	}

	public void setLimit(Integer limit){
		this.limit = limit;
	}

	public Integer getLimit(){
		return limit;
	}

	public void setCount(Integer count){
		this.count = count;
	}

	public Integer getCount(){
		return count;
	}

	public void setApi(String api){
		this.api = api;
	}

	public String getApi(){
		return api;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return status;
	}
}
