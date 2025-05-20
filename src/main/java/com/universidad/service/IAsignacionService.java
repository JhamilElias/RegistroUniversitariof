package com.universidad.service;


import com.universidad.model.Materia;
import com.universidad.model.Docente;

import java.util.List;

public interface IAsignacionService {

    // 1. Asignar una materia a un docente
    Materia asignarMateriaADocente(Long docenteId, Long materiaId);

    // 2. Obtener todas las materias asignadas a un docente
    List<Materia> obtenerMateriasPorDocente(Long docenteId);

    // 3. Obtener el docente asignado a una materia específica
    Docente obtenerDocentePorMateria(Long materiaId);

    // 4. Reasignar una materia a otro docente
    Materia reasignarMateria(Long materiaId, Long nuevoDocenteId);

    // 5. Eliminar la asignación de una materia
    void eliminarAsignacion(Long materiaId);

    // 6. Obtener todas las asignaciones (todas las materias con sus docentes)
    List<Materia> obtenerTodasAsignaciones();

    List<Materia> buscarAsignaciones(Long docenteId, String nombreMateria);

}
