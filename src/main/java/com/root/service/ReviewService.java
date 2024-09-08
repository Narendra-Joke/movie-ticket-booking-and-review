package com.root.service;

import com.root.domain.Movie;
import com.root.domain.Review;
import com.root.repository.MovieRepository;
import com.root.repository.ReviewRepository;
import com.root.service.response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    public void addReview(Review review) {
        Movie movie = movieRepository.findById(review.getMovie().getId()).orElse(null);
        reviewRepository.save(review);
        // needs to optimize
        // we need to create a scheduler for each 30 Min or 1 Hour
        if (movie != null){
            Double average = reviewRepository.getReviewAverage(movie.getId());
            movie.setRating(average);
            movieRepository.save(movie);
        }
    }

    public ReviewResponse getReviewById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.map(Review::toReviewResponse).orElse(null);
    }
}
