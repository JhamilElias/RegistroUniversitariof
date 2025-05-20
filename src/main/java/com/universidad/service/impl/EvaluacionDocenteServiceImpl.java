package com.universidad.service.impl;

import com.universidad.model.EvaluacionDocente;
import com.universidad.model.Materia;
import com.universidad.model.Docente;
import com.universidad.repository.EvaluacionDocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.repository.DocenteRepository;
import com.universidad.service.IEvaluacionDocenteService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionDocenteServiceImpl implements IEvaluacionDocenteService {
    @Autowired
    private EvaluacionDocenteRepository evaluacionDocenteRepository;
    @Autowired
    private DocenteRepository docenteRepository;

    @Override
    public EvaluacionDocente crearEvaluacion(EvaluacionDocente evaluacion) {
        return evaluacionDocenteRepository.save(evaluacion);
    }

    @Override
    public List<EvaluacionDocente> obtenerEvaluacionesPorDocente(Long docenteId) {
        Docente docente = docenteRepository.findById(docenteId).orElse(null);
        if (docente == null) return java.util.Collections.emptyList();
        return evaluacionDocenteRepository.findByDocente(docente);
    }

    @Override
    public EvaluacionDocente obtenerEvaluacionPorId(Long id) {
        return evaluacionDocenteRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarEvaluacion(Long id) {
        evaluacionDocenteRepository.deleteById(id);
    }



    @Autowired
    private MateriaRepository materiaRepository;

    

    @Transactional
    public Materia asignarMateriaADocente(Long docenteId, Long materiaId) {
    Docente docente = docenteRepository.findById(docenteId)
        .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
    
    Materia materia = materiaRepository.findById(materiaId)
        .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
    
    materia.setDocente(docente);
    return materiaRepository.save(materia);
    }

    public List<Materia> obtenerMateriasPorDocente(Long docenteId) {
        return materiaRepository.findByDocenteId(docenteId);
    }

    public Docente obtenerDocentePorMateria(Long materiaId) {
    return materiaRepository.findById(materiaId)
            .map(Materia::getDocente)
            .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
    }

    public Materia reasignarMateria(Long materiaId, Long nuevoDocenteId) {
    // 1. Buscar la materia
    Materia materia = materiaRepository.findById(materiaId)
            .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
    
    // 2. Buscar el nuevo docente
    Docente nuevoDocente = docenteRepository.findById(nuevoDocenteId)
            .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
    
    // 3. Asignar el objeto Docente completo (no solo el ID)
    materia.setDocente(nuevoDocente);
    
    // 4. Guardar los cambios
    return materiaRepository.save(materia);
    }

    @Transactional
    public void eliminarAsignacion(Long materiaId) {
        try {
            Materia materia = materiaRepository.findById(materiaId)
                    .orElseThrow(() -> new EntityNotFoundException("Materia no encontrada con ID: " + materiaId));
            
            if (materia.getDocente() == null) {
                throw new IllegalStateException("La materia no tiene docente asignado");
            }
            
            materia.setDocente(null);
            materiaRepository.save(materia);
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

    public List<Materia> obtenerTodasAsignaciones() {
    return materiaRepository.findByDocenteIsNotNull(); // Cambiar de findByDocenteIdIsNotNull a findByDocenteIsNotNull
    }
}
