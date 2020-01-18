package com.az.ratingdataservice.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.az.moviecatalogservice.models.CatalogItem;
import com.az.moviecatalogservice.models.Rating;
import com.az.moviecatalogservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class RatingService {

	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getFallBackUserRating",
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
					})
	public UserRating getUserRating(String userId) {
		return restTemplate.getForObject("http://rating-data-service/ratingsdata/users/"+userId, UserRating.class);
	}
	
	@HystrixCommand(fallbackMethod = "getFallBackUserRating",
			threadPoolKey = "ratingServicePool",
			threadPoolProperties  = {
					@HystrixProperty(name = "coorSize", value = "20"),
					@HystrixProperty(name = "maxQueueSize", value = "10"),					
					})
	public UserRating getUserRating_v2(String userId) {
		return restTemplate.getForObject("http://rating-data-service/ratingsdata/users/"+userId, UserRating.class);
	}
	
	private UserRating getFallBackUserRating(String userId) {
		UserRating userRating = new UserRating();
		userRating.setUserId(userId);
		userRating.setUserRating(Arrays.asList(new Rating("0", 0)));
		return userRating;
	}
}
