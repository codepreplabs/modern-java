package com.codeprep.model;

import java.time.Instant;

public record Movie(
        Double movie_id,
        String name,
        String cast,
        Integer year,
        Instant release_date
) {

}
