package com.minsait.mockito.repositories;

import com.minsait.mockito.models.Examen;

import java.util.List;
import java.util.Optional;

public interface ExmenRepository {
    List<Examen> findALl();
    Examen save(Examen examen);

}
