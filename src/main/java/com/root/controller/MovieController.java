package com.root.controller;

import com.root.domain.Genre;
import com.root.domain.Movie;
import com.root.service.MovieService;
import com.root.service.response.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/find")
    public MovieResponse findMovie(@RequestParam String title){
        return movieService.findMovie(title);
    }

    @GetMapping("/genre")
    public ResponseEntity<List<MovieResponse>> findMovieByGenre(@RequestParam String genre){
        List<MovieResponse> movieResponseList = movieService.findMovieByGenre(genre);
        if(!movieResponseList.isEmpty())
            return new ResponseEntity<>(movieResponseList, HttpStatus.OK);
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NOT_FOUND);
    }
}
