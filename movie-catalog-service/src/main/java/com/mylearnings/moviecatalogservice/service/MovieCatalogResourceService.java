package com.mylearnings.moviecatalogservice.service;

import com.mylearnings.moviecatalogservice.models.CatalogItem;
import com.mylearnings.moviecatalogservice.models.Movie;
import com.mylearnings.moviecatalogservice.models.Rating;
import com.mylearnings.moviecatalogservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieCatalogResourceService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;


    public List<CatalogItem> getCatalog(String userId) {
        UserRating userRating = getUserRating(userId);

        return userRating.getUserRatings().stream().map(rating -> getCatalogItem(rating)).collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
            threadPoolKey = "movieInfoPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            }
    )
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
    })
    public UserRating getUserRating(String userId) {
        UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);
        return userRating;
    }


    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }

    public UserRating getFallbackUserRating(String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setUserRatings(Arrays.asList(
                new Rating("0", 0)
        ));
        return userRating;
    }


}
/* Alternative Webclient way
* Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
*
*  */
