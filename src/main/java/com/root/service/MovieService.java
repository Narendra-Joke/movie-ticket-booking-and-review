package com.root.service;

import com.root.domain.Genre;
import com.root.domain.Movie;
import com.root.repository.MovieRepository;
import com.root.service.response.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieResponse findMovie(String title) {
        // handle non-exists scenario's
        // caching logic
        // exception handling
        Movie movie = movieRepository.findByTitle(title);
        if (Objects.nonNull(movie))
            return movie.toMovieResponse();
        return null;
    }

    public List<MovieResponse> findMovieByGenre(String genre) {
        if (Arrays.stream(Genre.values()).noneMatch(g -> g.toString().equals(genre)))
            return new ArrayList<>();

        List<Movie> movieList = movieRepository.findByGenre(Genre.valueOf(genre));
        if (!CollectionUtils.isEmpty(movieList)) {
            List<MovieResponse> movieResponseList = movieList.stream()
                    .sorted(Comparator.comparing(Movie::getRating, Comparator.reverseOrder()))
                    .map(m -> m.toMovieResponse())
                    .collect(Collectors.toList());
            if (movieResponseList.size() > 5)
                return movieResponseList.subList(0, 4);
            return movieResponseList;
        }
        return new ArrayList<>();
    }
}
