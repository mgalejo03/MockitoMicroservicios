package com.minsait.mockito.services;

import com.minsait.mockito.models.Examen;
import com.minsait.mockito.repositories.ExmenRepository;
import com.minsait.mockito.repositories.PreguntaRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {
    ExmenRepository exmenRepository;
    PreguntaRepository preguntaRepository;

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        exmenRepository.findALl();
        return exmenRepository.findALl().stream()
                .filter(examen -> examen.getNombre().equals(nombre))
                .findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examen = findExamenPorNombre(nombre);
        if (examen.isPresent()){
            examen.get().setPreguntas(preguntaRepository
            .findPreguntasByExamenId(examen.get().getId()));
            return examen.get();
        }
        return null;
    }

    @Override
    public Examen save(Examen examen) {
        exmenRepository.save(examen);
        if (!examen.getPreguntas().isEmpty()){
            preguntaRepository.savePreguntas(examen.getPreguntas());
        }
        return exmenRepository.save(examen);
    }
}
