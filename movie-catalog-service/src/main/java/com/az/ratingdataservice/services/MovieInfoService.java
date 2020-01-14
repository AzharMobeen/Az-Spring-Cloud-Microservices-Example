package com.az.ratingdataservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.az.moviecatalogservice.models.CatalogItem;
import com.az.moviecatalogservice.models.Movie;
import com.az.moviecatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class MovieInfoService {

	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getFallBackCatalogItem")
	public CatalogItem getCatalogItemByRating(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getMovieName(),movie.getDescription(),rating.getRating());
	}
	
	private CatalogItem getFallBackCatalogItem(Rating rating) {
		CatalogItem catalogItem = new CatalogItem("No Movie", "-", rating.getRating());
		return catalogItem;
	}
}
