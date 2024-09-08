package com.root.domain;

import java.io.Serializable;
import java.util.List;

import com.root.service.response.MovieResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="movie_table")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Serializable {
	@Id
	@Column(name="id",nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	@Enumerated(EnumType.STRING)
	private Genre genre;
	
	private Double rating; // a single entity which is average rating of all reviews for a movie
	
	@OneToMany(mappedBy="movie")
	private List<Review> reviews;

	public MovieResponse toMovieResponse(){
		return MovieResponse.builder()
				.genre(this.genre)
				.title(this.title)
				.rating(this.rating)
				.reviews(Review.toReviewResponse(this.reviews))
				.build();
	}
}
