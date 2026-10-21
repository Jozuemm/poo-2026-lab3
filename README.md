### Laboratorio 3

## Análisis

# Problema

La empresa TecniHogar necesita un programa que permita administrar sus servicios tecnicos a domicilio. Actualmente utuliza una hoja calculo, pero las diferencias entre los servicios han provocado errores en los cobros y en la programacipon de las visitas
El sistema debe manejar diagnósticos, instalaciones, instalaciones eléctricas certificadas y mantenimientos de aire acondicionado. Estas categorías comparten información general, pero tienen diferentes reglas de costo, tiempo de atención, compatibilidad con cuadrillas y cantidad máxima de visitas
También se necesita organizar las órdenes en jornadas de trabajo, controlar sus estados y registrar los ingresos. Para resolver el problema se utilizarán herencia y polimorfismo, de manera que cada categoría sea responsable de sus propias reglas

# Entradas

- Orden de servicio: Código, nombre del cliente, dirección, horas estimadas y zona
- Instalación: Cantidad de puntos que se instalarán
- Instalación electrica certificada: Cantidad de puntos y valor declarado de los materiales
- Mantenimiento de aire acondicionado: Capacidad del equipo en BTU
- Cuadrilla: Código, capacidad máxima de horas por jornada e indicación de certificación en refrigeración
- Jornada: Cuadrilla seleccionada y órdenes que se desean asignar o retirar
- Resultado de visita: Orden atendida y resultado: completada o fallida
- Menu: Opción correspondiente a la operación que desea realizar el usuario

# Procesos

- Cotización y registro de ordenes
- Calculo de costos y tiempos
- Registro de cuadrillas y creacion de jornadas
- Asignación y retiro de ordenes
- Inicio de jornadas y registro de visitas
- Registro de resultados y reprogramación
- Generación de reportes

# Salidas

- Cotizaciones con costo y plazo estimado de atención
- Confirmaciones de las operaciones realizadas
- Listados de órdenes y cuadrillas
- Estado y cantidad de visitas de cada orden
- Jornadas abiertas con sus horas asignadas y disponibles
- Reportes de órdenes e ingresos
- Mensajes claros cuando los datos o las operaciones sean inválidos

# Clases

- OrdenDeServicio: Clase abstracta que reúne los datos comunes y controla los estados, visitas e ingresos de una orden
- Diagnostico: Define las reglas de costo, plazo y visitas del diagnóstico y valida el límite de horas
- Instalacion: Conserva los puntos y calcula las horas facturables, el costo, el plazo y el límite de visitas
- InstalacionElectricaCertificada: Extiende la instalación incorporando materiales, certificado, garantía y sus reglas particulares de plazo y visitas
- MantenimientoAire: Conserva los BTU y establece las reglas del mantenimiento y su compatibilidad con cuadrillas
- Cuadrilla: Representa la capacidad de trabajo y certificación del equipo técnico
- Jornada: Administra las asignaciones, verifica capacidad y compatibilidad, inicia visitas y registra sus resultados
- Controller: Mantiene las colecciones, valida identificadores únicos y coordina operaciones, consultas y reportes
- Vista: Presenta el menú, recibe entradas y muestra mensajes y resultados
- Main: Crea los componentes necesarios e inicia la aplicación