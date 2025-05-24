package com.universidad.controller;

import com.universidad.model.Docente;
import com.universidad.model.EvaluacionDocente;
import com.universidad.model.Materia;
//import com.universidad.service.impl.*;
import com.universidad.service.IEvaluacionDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IAsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RestController
@RequestMapping("/api/docentes-materias")
public class EvaluacionDocenteController {
    
    
    @Autowired
    private IEvaluacionDocenteService evaluacionDocenteService;
    @Autowired
    private IAsignacionService asignacionService;

    @PostMapping
    public ResponseEntity<EvaluacionDocente> crearEvaluacion(@RequestBody EvaluacionDocente evaluacion) {
        EvaluacionDocente nueva = evaluacionDocenteService.crearEvaluacion(evaluacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    /*@GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<EvaluacionDocente>> obtenerEvaluacionesPorDocente(@PathVariable Long docenteId) {
        List<EvaluacionDocente> evaluaciones = evaluacionDocenteService.obtenerEvaluacionesPorDocente(docenteId);
        return ResponseEntity.ok(evaluaciones);
    }*/
    @PreAuthorize("hasAnyRole('ROLE_ESTUDIANTE', 'ROLE_DOCENTE', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDocente> obtenerEvaluacionPorId(@PathVariable Long id) {
        EvaluacionDocente evaluacion = evaluacionDocenteService.obtenerEvaluacionPorId(id);
        if (evaluacion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(evaluacion);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvaluacion(@PathVariable Long id) {
        evaluacionDocenteService.eliminarEvaluacion(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para asignar una materia a un docente
     * POST /api/docentes-materias/asignar?docenteId={id}&materiaId={id}
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/asignar")
    public ResponseEntity<Materia> asignarMateriaADocente(
            @RequestParam Long docenteId,
            @RequestParam Long materiaId) {
        try {
            Materia materiaAsignada = asignacionService.asignarMateriaADocente(docenteId, materiaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(materiaAsignada);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint para obtener todas las materias asignadas a un docente
     * GET /api/docentes-materias/docente/{docenteId}
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<Materia>> obtenerMateriasPorDocente(
            @PathVariable Long docenteId) {
        try {
            List<Materia> materias = asignacionService.obtenerMateriasPorDocente(docenteId);
            return ResponseEntity.ok(materias);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint para obtener el docente asignado a una materia
     * GET /api/docentes-materias/materia/{materiaId}
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/materia/{materiaId}")
    public ResponseEntity<Docente> obtenerDocentePorMateria(
            @PathVariable Long materiaId) {
        try {
            Docente docente = asignacionService.obtenerDocentePorMateria(materiaId);
            return ResponseEntity.ok(docente);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint para reasignar una materia a otro docente
     * PUT /api/docentes-materias/{materiaId}/reasignar?nuevoDocenteId={id}
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{materiaId}/reasignar")
    public ResponseEntity<Materia> reasignarMateria(
            @PathVariable Long materiaId,
            @RequestParam Long nuevoDocenteId) {
        try {
            Materia materia = asignacionService.reasignarMateria(materiaId, nuevoDocenteId);
            return ResponseEntity.ok(materia);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Endpoint para eliminar la asignación de una materia
     * DELETE /api/docentes-materias/{materiaId}
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{materiaId}")
    public ResponseEntity<Void> eliminarAsignacion(
            @PathVariable Long materiaId) {
        try {
            asignacionService.eliminarAsignacion(materiaId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint para listar todas las asignaciones activas
     * GET /api/docentes-materias
     */
    
    @GetMapping
    public ResponseEntity<List<Materia>> obtenerTodasAsignaciones() {
        try {
            List<Materia> materias = asignacionService.obtenerTodasAsignaciones();
            return ResponseEntity.ok(materias);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Endpoint para buscar asignaciones por criterios (ejemplo adicional)
     * GET /api/docentes-materias/buscar?docenteId={id}&materiaNombre={nombre}
     */
    @PreAuthorize("hasAnyRole('ROLE_ESTUDIANTE', 'ROLE_DOCENTE', 'ROLE_ADMIN')")
    @GetMapping("/buscar")
    public ResponseEntity<List<Materia>> buscarAsignaciones(
            @RequestParam(required = false) Long docenteId,
            @RequestParam(required = false) String materiaNombre) {
        try {
            List<Materia> materias = asignacionService.buscarAsignaciones(docenteId, materiaNombre);
            return ResponseEntity.ok(materias);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
