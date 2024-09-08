package com.root.service.request;

import com.root.domain.Genre;
import com.root.domain.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    private String title;

    private Genre genre;

    public Movie toMovie(){
        return Movie.builder()
                .title(title)
                .genre(genre)
                .rating(0.0)
                .build();
    }
}
