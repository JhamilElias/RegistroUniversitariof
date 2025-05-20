package com.universidad.service.impl;

import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IAsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.universidad.service.*;

import java.util.List;

@Service
public class AsignacionServiceImpl implements IAsignacionService {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Override
    public Materia asignarMateriaADocente(Long docenteId, Long materiaId) {
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        materia.setDocente(docente); // Asegúrate de que la clase Materia tenga setDocente
        return materiaRepository.save(materia);
    }

    @Override
    public List<Materia> obtenerMateriasPorDocente(Long docenteId) {
        return materiaRepository.findByDocenteId(docenteId);
    }

    @Override
    public Docente obtenerDocentePorMateria(Long materiaId) {
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        return materia.getDocente();
    }

    @Override
    public Materia reasignarMateria(Long materiaId, Long nuevoDocenteId) {
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        Docente nuevoDocente = docenteRepository.findById(nuevoDocenteId)
                .orElseThrow(() -> new RuntimeException("Nuevo docente no encontrado"));
        materia.setDocente(nuevoDocente);
        return materiaRepository.save(materia);
    }

    @Override
    public void eliminarAsignacion(Long materiaId) {
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        materia.setDocente(null);
        materiaRepository.save(materia);
    }

    @Override
    public List<Materia> obtenerTodasAsignaciones() {
        return materiaRepository.findAll();
    }

    @Override
    public List<Materia> buscarAsignaciones(Long docenteId, String nombreMateria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarAsignaciones'");
    }

    
}
