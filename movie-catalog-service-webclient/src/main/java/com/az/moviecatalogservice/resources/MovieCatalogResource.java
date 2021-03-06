package com.az.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.az.moviecatalogservice.models.CatalogItem;
import com.az.moviecatalogservice.models.Movie;
import com.az.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId){
		
		UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+userId, UserRating.class);
		
		return ratings.getUserRating().stream().map(rating-> {
			//Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);
			Movie movie = webClientBuilder.build()
					.get()
					.uri("http://localhost:8082/movies/"+rating.getMovieId())
					.retrieve()
					.bodyToMono(Movie.class)
					.block();
			return new CatalogItem(movie.getMovieName(),"Test",rating.getRating());
		}).collect(Collectors.toList());		
	}
}
