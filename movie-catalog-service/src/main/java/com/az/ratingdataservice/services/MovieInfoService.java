package com.az.ratingdataservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.az.moviecatalogservice.models.CatalogItem;
import com.az.moviecatalogservice.models.Movie;
import com.az.moviecatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class MovieInfoService {

	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getFallBackCatalogItem",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
					})
	public CatalogItem getCatalogItemByRating(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getMovieName(),movie.getDescription(),rating.getRating());
	}
	
	@HystrixCommand(fallbackMethod = "getFallBackCatalogItem",
			threadPoolKey = "movieInfoPool",
			threadPoolProperties  = {
					@HystrixProperty(name = "coorSize", value = "20"),
					@HystrixProperty(name = "maxQueueSize", value = "10"),					
					})
	public CatalogItem getCatalogItemByRating_v2(Rating rating) {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getMovieName(),movie.getDescription(),rating.getRating());
	}
	
	
	private CatalogItem getFallBackCatalogItem(Rating rating) {
		CatalogItem catalogItem = new CatalogItem("No Movie", "-", rating.getRating());
		return catalogItem;
	}
}
