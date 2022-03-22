package com.janadri.moviecatalogservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.janadri.moviecatalogservice.models.CatalogItem;
import com.janadri.moviecatalogservice.models.Movie;
import com.janadri.moviecatalogservice.models.Rating;
import com.janadri.moviecatalogservice.models.UserRating;
import com.janadri.moviecatalogservice.services.MovieInfo;
import com.janadri.moviecatalogservice.services.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	WebClient.Builder webClientBuilder;
	
	@Autowired
	MovieInfo movieInfo;
	
	@Autowired
	UserRatingInfo userRatingInfo;
	

//whenever we get timeout issue or if any other microservice/s are down/slow, break the circuit and call the fallback method    
	@RequestMapping("/{userId}")
//    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating userRating =userRatingInfo.getUserRating(userId);

		return userRating.getRatings().stream().map(rating ->movieInfo.getCatalogItem(rating)).collect(Collectors.toList());

	}

	

// fallback method    
//    public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
//    	return Arrays.asList(new CatalogItem("No Movie", "", 0));
//    }

}
/*
 * Alternative WebClient way Movie movie =
 * webClientBuilder.build().get().uri("http://localhost:8082/movies/"+
 * rating.getMovieId()) .retrieve().bodyToMono(Movie.class).block();
 */