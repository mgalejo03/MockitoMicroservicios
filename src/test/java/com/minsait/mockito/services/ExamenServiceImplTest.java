package com.minsait.mockito.services;

import com.minsait.mockito.models.Examen;
import com.minsait.mockito.repositories.ExmenRepository;
import com.minsait.mockito.repositories.PreguntaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
    @Mock
    ExmenRepository exmenRepository;
    @Mock
    PreguntaRepository preguntaRepository;
    @InjectMocks
    ExamenServiceImpl service;
    @Captor
    ArgumentCaptor<Long> captor;

    @Test
    void testArgumentCaptor(){
        when(exmenRepository.findALl()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Historia");
        verify(preguntaRepository).findPreguntasByExamenId(captor.capture());

        assertEquals(3L, captor.getValue());

    }

    @Test
    void testFindExamenPorNombre() {
        //Datos necesarios simulados
        when(exmenRepository.findALl()).thenReturn(Datos.EXAMENES);

        //llama al metodo
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");

        //Prueba unitaria o l afirmación de que el examen se ha obtenido correctamente
        assertTrue(examen.isPresent());
    }

    @Test
    void testPreguntasExamen(){
        when(exmenRepository.findALl()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Historia");

        assertTrue(examen.getPreguntas().contains("Aritmetica"));

        verify(exmenRepository, atLeast(2)).findALl(); //times, atMostOnce, atLeastOnce, atLeast
        verify(preguntaRepository, times(1)).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testExceptions(){
        when(exmenRepository.findALl()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class,()->{
            service.findExamenPorNombreConPreguntas("Matemáticas");
        });
        assertTrue(IllegalArgumentException.class.equals(exception.getClass()));
    }

    @Test
    void testDoThrow(){
        doThrow(IllegalArgumentException.class).when(preguntaRepository).savePreguntas(anyList());
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        assertThrows(IllegalArgumentException.class, () -> service.save(examen));
    }

    @Test
    void testDoAnswer(){
        when(exmenRepository.findALl()).thenReturn(Datos.EXAMENES);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return  id ==1?Datos.PREGUNTAS: Collections.EMPTY_LIST;
        }).when(preguntaRepository).findPreguntasByExamenId(anyLong());
        /**when(preguntaRepository.findPreguntasByExamenId(1L)).thenReturn(Datos.PREGUNTAS);
        when(preguntaRepository.findPreguntasByExamenId(2L)).thenReturn(Datos.PREGUNTAS);**/

        Examen examen = service.findExamenPorNombreConPreguntas("Español");
        assertAll(
                () -> assertNotNull(examen),
                () -> assertTrue(examen.getPreguntas().isEmpty()),
                () -> assertEquals(0, examen.getPreguntas().size())
        );
    }
    @Test
    void testSave(){
        when(exmenRepository.save(Datos.EXAMEN)).thenReturn(Datos.EXAMEN);
        Examen examenSave = service.save(Datos.EXAMEN);
        assertNotNull(examenSave);
        assertEquals(Examen.class, service.save(examenSave).getClass());
        assertEquals("Química",examenSave.getNombre());
       assertEquals(4L, examenSave.getId());
    }

    @Test
    void testReturnNull(){
        when(exmenRepository.findALl()).thenReturn(Datos.EXAMENES);
        Examen examenVacio = service.findExamenPorNombreConPreguntas(" ");
        assertNull(examenVacio);
    }
}