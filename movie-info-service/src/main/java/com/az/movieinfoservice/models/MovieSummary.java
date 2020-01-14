package com.az.movieinfoservice.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieSummary implements Serializable{

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "original_title")
	private String title;
	@JsonProperty(value = "overview")
	private String summary;
}
