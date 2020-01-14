package com.az.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.az.moviecatalogservice.models.CatalogItem;
import com.az.moviecatalogservice.models.UserRating;
import com.az.ratingdataservice.services.MovieInfoService;
import com.az.ratingdataservice.services.RatingService;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private MovieInfoService movieInfoService;
	
	@RequestMapping("/{userId}")	
	public List<CatalogItem> getCatalog(@PathVariable String userId){						
		UserRating ratings = ratingService.getUserRating(userId);		
		return ratings.getUserRating().stream()
				.map(rating-> movieInfoService.getCatalogItemByRating(rating))
				.collect(Collectors.toList());		
	}

	

	
	
	// This method will call in case of circuit break happens as fallback
	public List<CatalogItem> getFallBackCatalog(@PathVariable String userId){
	
		return Arrays.asList(new CatalogItem("No Movie", "", 0));
	}
}
