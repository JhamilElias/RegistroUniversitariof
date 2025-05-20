Para poder efectuar las pruebas correctamente primero debemos incorporar las siguiente tablas
para el respectivo inicio de sesion y manejo de roles


########################
CREATE TABLE SPRING_SESSION (
PRIMARY_ID CHAR(36) NOT NULL,
SESSION_ID CHAR(36) NOT NULL,
CREATION_TIME BIGINT NOT NULL,
LAST_ACCESS_TIME BIGINT NOT NULL,
MAX_INACTIVE_INTERVAL INT NOT NULL,
EXPIRY_TIME BIGINT NOT NULL,
PRINCIPAL_NAME VARCHAR(100),
CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);
CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);
CREATE TABLE SPRING_SESSION_ATTRIBUTES (
SESSION_PRIMARY_ID CHAR(36) NOT NULL,
ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
ATTRIBUTE_BYTES BYTEA NOT NULL,
CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID,
ATTRIBUTE_NAME),
CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES
SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);

########################################################################################


Una vez instaurado dichas tablas prodecemos a la insercion de datos para las pruebas visibles
en POSTMAN
(Ejecutamos en pgadmin4)


########################################################################################

INSERT INTO persona (id_persona, apellido, email, fecha_nacimiento, nombre, version)
VALUES
(1, 'Pérez', 'cperez@email.com', '1980-05-10', 'Carlos', 1),
(2, 'García', 'agarcia@email.com', '2003-03-15', 'Ana', 1),
(3, 'Torres', 'ltorres@email.com', '2004-08-22', 'Luis', 1),
(4, 'Sánchez', 'msanchez@email.com', '2003-12-01', 'María', 1);


--docente
-- Inserción de datos en la tabla departamento
INSERT INTO docente (departamento, nro_empleado, id_persona) VALUES
('Ventas', 'EMP-001', 1),
('Recursos Humanos', 'EMP-002', 2)


--tabla estudiante
-- Inserción de datos en la tabla estudiante
INSERT INTO estudiante (
    id_persona,
    numero_inscripcion,
    estado,
    fecha_alta,
    fecha_baja,
    fecha_modificacion,
    motivo_baja,
    usuario_alta,
    usuario_baja,
    usuario_modificacion
) VALUES

-- Estudiante que dio de baja
(3, 'EST-2023-003', 'Baja', '2023-09-01', '2023-11-15', '2023-11-15', 'Cambio de carrera', 'admin', 'director', 'director'),

-- Estudiante inactivo
(4, 'EST-2023-004', 'Inactivo', '2023-09-01', NULL, '2023-10-20', NULL, 'admin', NULL, 'mgomez');



--materia
-- Inserción de datos en la tabla materia
INSERT INTO materia (
    id_materia,
    codigo_unico,
    creditos,
    nombre_materia,
    version,
    docente_id
) VALUES
-- Materias de Matemáticas (docente_id 1)
(1, 'MAT-101', 4, 'Cálculo Diferencial', 1, 1),
(2, 'MAT-102', 4, 'Álgebra Lineal', 1, 1),
(3, 'MAT-201', 5, 'Cálculo Integral', 2, 1),

-- Materias de Literatura (docente_id 2)
(4, 'LET-101', 3, 'Literatura Universal', 1, 2),
(5, 'LET-202', 3, 'Análisis Literario', 2, 2),
(6, 'LET-301', 4, 'Literatura Contemporánea', 1, 2)

--inscripciones
-- Inserción de datos en la tabla inscripciones
INSERT INTO inscripciones (
    id,
    fecha_inscripcion,
    estudiante_id,
    materia_id
) VALUES
-- Inscripciones para el estudiante 1 (3 materias)
(1, '2023-09-01', 3, 1),  
(2, '2023-09-01', 3, 4),  -- Literatura Universal
(3, '2023-09-02', 4, 6),  -- Física General

-- Inscripciones para el estudiante 2 (2 materias)
(4, '2023-09-01', 3, 2),  -- Álgebra Lineal
(5, '2023-09-01', 4, 5)  -- Análisis Literario


--tabla materia_prerequisito

-- Inserción de datos en la tabla materia_prerequisito
INSERT INTO materia_prerequisito (
    id_materia,
    id_prerequisito
) VALUES
-- Para Cálculo Integral (MAT-201) se requiere Cálculo Diferencial (MAT-101)
(3, 1),

-- Para Análisis Literario (LET-202) se requiere Literatura Universal (LET-101)
(5, 4),

-- Para Literatura Contemporánea (LET-301) se requiere Análisis Literario (LET-202)
(6, 5)



--tabla estudiante_materia
-- Inserción de datos en la tabla estudiante_materia
INSERT INTO estudiante_materia (
    id_estudiante,
    id_materia
) VALUES

-- Estudiante 3 (Carlos López) inscrito en 2 materias
(3, 3),  
(3, 5),  


-- Estudiante 4 (Ana Martínez) inscrito en 1 materia
(4, 6)

