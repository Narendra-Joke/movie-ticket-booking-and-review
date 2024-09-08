package com.root.service;

import com.root.domain.*;
import com.root.exception.NotFoundException;
import com.root.repository.MovieRepository;
import com.root.repository.ShowRepository;
import com.root.repository.ShowSeatsRepository;
import com.root.repository.TheaterRepository;
import com.root.resource.ShowResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ShowSeatsRepository showSeatsRepository;

    public ShowService() {

    }

    public List<ShowResource> searchShows(String movieName, String cityName, String theaterName) {
        if (!StringUtils.hasText(cityName))
            new ArrayList<>();
        List<Show> shows = new ArrayList<>();

        if (StringUtils.hasText(movieName))
            shows = showRepository.findByMovieNameAndCity(movieName, cityName);
        else if (StringUtils.hasText(theaterName))
            shows = showRepository.findByTheaterAndCity(theaterName, cityName);
        else
            shows = showRepository.findByCity(cityName);

        if (CollectionUtils.isEmpty(shows))
            return new ArrayList<>();
        else
            return shows.stream().map(Show::toResource).collect(Collectors.toList());
    }

    public ShowResource addShow(ShowResource showResource) {
        Optional<Movie> optionalMovie = movieRepository.findById(showResource.getMovieId());

        if (!optionalMovie.isPresent()) {
            throw new NotFoundException("Movie Not Found with ID: " + showResource.getMovieId() + " to add New Show");
        }


        Optional<Theater> optionalTheater = theaterRepository.findById(showResource.getTheaterId());
        if (!optionalTheater.isPresent()) {
            throw new NotFoundException("Theater Not Found with ID: " + showResource.getTheaterId() + " to add New Show");
        }
        log.info("Adding New Show : " + showResource);

        Show show = Show.toEntity(showResource);

        show.setMovie(optionalMovie.get());
        show.setTheater(optionalTheater.get());
        show.setSeats(generateShowSeats(show.getTheater().getSeats(), show));

        for (ShowSeat seatsEntity : show.getSeats()) {
            seatsEntity.setShow(show);
        }

        show = showRepository.save(show);

        return Show.toResource(show);

    }

    private List<ShowSeat> generateShowSeats(List<TheaterSeats> theaterSeatsEntities, Show show) {
        List<ShowSeat> showSeatsEntities = new ArrayList<>();
        for (TheaterSeats theaterSeats : theaterSeatsEntities) {
            ShowSeat showSeat = ShowSeat.builder()
                    .seatNumber(theaterSeats.getSeatNumber())
                    .seatType(theaterSeats.getSeatType())
                    .rate(100)  // add logic to update seat rate here
                    .build();

            showSeatsEntities.add(showSeat);
        }
        return showSeatsRepository.saveAll(showSeatsEntities);
    }
}
