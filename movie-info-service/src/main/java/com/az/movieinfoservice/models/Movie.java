package com.az.movieinfoservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

	private String movieId;
	private String movieName;
	private String description;
	
	public Movie(String movieId,String movieName) {		
		this.movieId = movieId;
		this.movieName = movieName;
	}	
	
}
