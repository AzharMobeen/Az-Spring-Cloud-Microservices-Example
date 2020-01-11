package com.az.ratingdataservice.models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRating {

	private List<Rating> userRating;	
}
