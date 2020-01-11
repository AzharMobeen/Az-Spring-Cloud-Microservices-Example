package com.az.ratingdataservice.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.az.ratingdataservice.models.Rating;
import com.az.ratingdataservice.models.UserRating;

@RestController
@RequestMapping("/ratingsdata")
public class RatingDataResource {

	@RequestMapping("/{movieId}")
	public Rating getRatingData(@PathVariable String movieId) {
	
		return new Rating(movieId, 4);
	}
	
	@RequestMapping("users/{userId}")
	public UserRating getUserRatingData(@PathVariable String userId) {
		
		List<Rating> ratings = Arrays.asList(
				new Rating("1122",4),
				new Rating("3344",5),
				new Rating("5566",6),
				new Rating("7788",8)
				);
		
		UserRating userRating = new UserRating();
		userRating.setUserRating(ratings);
		return userRating;
	}
}
