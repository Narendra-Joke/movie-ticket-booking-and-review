package com.root.service.response;

import com.root.domain.Genre;
import com.root.domain.Review;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class MovieResponse {
    private String title;

    private Genre genre;

    private Double rating;

    private List<ReviewResponse> reviews;
}
