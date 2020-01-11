package com.az.movieinfoservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Movie {

	private String movieId;
	private String movieName;	
	
	public Movie(String movieId,String movieName) {		
		this.movieId = movieId;
		this.movieName = movieName;
	}	
	
}
