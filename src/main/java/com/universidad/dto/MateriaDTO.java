package com.universidad.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

import com.universidad.service.IMateriaService;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MateriaDTO {

    private Long id;

    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String nombreMateria;

    @NotBlank(message = "El código único es obligatorio")
    @Size(max = 10, message = "El código único no debe superar los 10 caracteres")
    private String codigoUnico;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Debe tener al menos 1 crédito")
    @Max(value = 10, message = "No puede tener más de 10 créditos")
    private Integer creditos;

    private Long docenteId;

    private List<Long> prerequisitos;
    private List<Long> esPrerequisitoDe;


    /*public boolean formariaCirculo(Long prerequisitoId) {
        // Implementa la lógica aquí usando solo los IDs de prerequisitos
        return this.getPrerequisitos() != null 
            && this.getPrerequisitos().contains(prerequisitoId);
    }*/

    public boolean formariaCirculo(Long prerequisitoId, IMateriaService materiaService) {
        if (prerequisitoId.equals(this.id)) {
            return true; // Un prerequisito no puede ser la misma materia
        }
        
        // Verifica si el prerequisito ya está en la lista directa
        if (this.prerequisitos != null && this.prerequisitos.contains(prerequisitoId)) {
            return true;
        }
        
        // Verificación recursiva de prerequisitos
        if (this.prerequisitos != null) {
            for (Long prereqId : this.prerequisitos) {
                MateriaDTO prereq = materiaService.obtenerMateriaPorId(prereqId);
                if (prereq != null && prereq.formariaCirculo(prerequisitoId, materiaService)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
