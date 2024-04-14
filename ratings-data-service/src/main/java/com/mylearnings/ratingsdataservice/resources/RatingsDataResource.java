package com.mylearnings.ratingsdataservice.resources;

import com.mylearnings.ratingsdataservice.models.Rating;
import com.mylearnings.ratingsdataservice.models.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsDataResource {

    @GetMapping("/{movieId}")
    public Rating getRating(@PathVariable String movieId) {
        return new Rating(movieId, 4);
    }

    @GetMapping("/users/{userId}")
    public UserRating getUserRating(@PathVariable String userId) {
        UserRating userRating = new UserRating();
        userRating.initData(userId);
        return userRating;
    }
}
