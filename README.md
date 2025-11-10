# Evaluación 02 — CGE Electricidad (KMP: Web + Desktop)

Aplicación Kotlin Multiplatform que permite:
- Registrar clientes, medidores (monofásicos y trifásicos) y lecturas de consumo (kWh).
- Generar boletas mensuales aplicando tarifas y cargos, con opción de exportar a PDF.
- Mantener persistencia local de datos en una capa separada de la UI y del dominio.

## 1) Requisitos cubiertos (según pauta)

- Lenguaje/Framework: Kotlin Multiplatform (KMP) con Compose Multiplatform (Desktop/Web).
- Dominio implementado: Cliente, Operador, Medidor (Monofásico/Trifásico), LecturaConsumo, Boleta, Tarifa (Residencial/Comercial) y sus detalles.
- POO: herencia y polimorfismo en Medidor (base → Monofásico/Trifásico) y Tarifa (base → Residencial/Comercial). Encapsulamiento en entidades y servicios.
- Persistencia (DAO/repositorios): capa “persistencia” desacoplada, con almacenamiento local mediante un driver de storage; datos de Clientes, Medidores y Lecturas se guardan y se recuperan entre ejecuciones.
- UI: Compose Desktop completa y vistas Web preparadas (Wasm/JS). La navegación y pantallas siguen la misma lógica de negocio.
- PDF: Servicio de generación de PDF con tabla de boleta (RUT, Mes, Año, kWh, Subtotal, Cargos, IVA y Total).
- Filtros: por RUT en vistas relacionadas (Clientes, Medidores y Lecturas), y por código en Medidores.

Extras (parciales):
- Estructura modular por capas (dominio, persistencia, servicios, ui).
- Validaciones en formularios (RUT/cliente existente, formato de números, etc.).

Pendientes/Margen de mejora para puntaje máximo:
- Alternancia de tema claro/oscuro a nivel global (toggle).
- Paridad funcional completa en Web si hay pantallas pendientes de portar 1:1.
- Secuenciación determinística del “código de medidor” (actualmente se genera automáticamente en UI); moverlo al repositorio y asegurar unicidad/consistencia.
- Filtros adicionales (por nombre de cliente en listado, por rango de fechas en lecturas).
- Tests unitarios de servicios/repositorios.
- Documento UML/resumen de diseño (según rúbrica).

## 2) Arquitectura

- Dominio: Entidades de negocio (Cliente, Medidor, etc.) y modelos de cálculo (Tarifa, Boleta).
- Servicios: Orquestación de caso de uso (generación de boleta, generación de PDF, cálculo según tipo de tarifa).
- Persistencia: Repositorios por agregado (Clientes, Medidores, Lecturas, Boletas) y una abstracción `PersistenciaDatos`/driver de storage.
- UI: Pantallas Compose con estado y validaciones de entrada. Compartido entre targets KMP.

## 3) Persistencia

- Los datos se almacenan localmente mediante una implementación de almacenamiento clave/valor.
- Se persisten: Clientes, Medidores y Lecturas. Boletas pueden persistirse (dependiendo de uso); PDF se exporta en disco.
- Los repositorios aíslan a la UI del detalle de almacenamiento.
- Claves por entidad (e.g., `cliente_{rut}`, `medidor_{codigo}`, `lectura_{codigoMedidor}_{año}_{mes}`).
- Los datos se escriben al crear/actualizar, por lo que quedan disponibles al reiniciar la app.

## 4) Ejecución

Desktop (JVM):
- macOS/Linux:
  ```bash
  ./gradlew :composeApp:run
  ```
- Windows:
  ```powershell
  .\gradlew.bat :composeApp:run
  ```

Web:
- Wasm (recomendado, moderno):
  - macOS/Linux:
    ```bash
    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
    ```
  - Windows:
    ```powershell
    .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
    ```
- JS (compatibilidad extendida):
  - macOS/Linux:
    ```bash
    ./gradlew :composeApp:jsBrowserDevelopmentRun
    ```
  - Windows:
    ```powershell
    .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
    ```

## 5) Guía de uso

1. Clientes:
   - Ir a “Clientes”.
   - Registrar cliente con RUT, nombre, email, dirección de facturación y tipo.
   - El registro queda persistido automáticamente.

2. Medidores:
   - Ir a “Medidores”.
   - Ingresar RUT para listar o crear medidores asociados.
   - Crear medidor:
     - Elegir tipo (Monofásico/Trifásico), estado (Activo/No activo), dirección y potencia máx (kW).
     - Si es Trifásico, ingresar el factor de potencia.
     - El código del medidor se genera automáticamente y queda persistido.

3. Lecturas:
   - Ir a “Lecturas”.
   - Ingresar RUT y el código del medidor para filtrar.
   - Registrar lectura con Año, Mes y kWh leídos.
   - Las lecturas se guardan y pueden consultarse por medidor/mes.

4. Boletas:
   - Ir a “Boletas”.
   - Seleccionar cliente/medidor y período.
   - Generar boleta aplicando la tarifa según tipo (Residencial/Comercial) con cargos, IVA, y total.
   - Exportar PDF: se genera una tabla con los datos de la boleta (RUT, Mes, Año, kWh, Subtotal, Cargos, IVA y Total).

## 6) Validaciones y reglas

- RUT debe existir para asociar medidores y registrar lecturas.
- En medidores: potencia máx requerida; factor de potencia requerido para trifásicos.
- En lecturas: Año/Mes numéricos válidos; kWh numérico.
- Eliminación: se puede eliminar medidor seleccionado (persistencia se actualiza).

## 7) Cumplimiento de la rúbrica (resumen)

- Diseño OO: herencia y polimorfismo en Medidores y Tarifas; entidades encapsuladas y separadas por capas.
- Uso de herencia/polimorfismo: correcto y justificado en el modelo.
- Encapsulamiento/abstracción: repositorios y servicios exponen interfaces; la UI no conoce detalles de almacenamiento.
- Estructura modular: separación por paquetes (dominio, persistencia, servicios, ui).
- Cálculo/Lógica de boleta: diferenciación por tipo de tarifa, cargos e IVA.
- Generación de PDF: reporte con tabla y totales.
- Documentación: este README describe arquitectura, ejecución y flujo.

Pendientes para “destacado”:
- Tema oscuro con toggle global.
- Paridad completa de pantallas en Web (si faltase alguna).
- Código de medidor determinístico en repositorio (evitar posibles colisiones y mantener secuencia definida).
- Filtros adicionales (por nombre de cliente, rango temporal en lecturas).
- Tests unitarios y UML.

## 8) Roadmap de mejoras sugeridas

- Mover la generación del código del medidor al repositorio, asegurando unicidad (p.ej. contar existentes y formatear `MONO-0001`, `TRI-0001`).
- Implementar tema oscuro global (Material 3 + toggle en la barra superior).
- Paridad total en Web: portar las pantallas restantes 1:1 desde Desktop.
- Añadir edición de medidores y validaciones cruzadas (p.ej., no permitir lecturas duplicadas por (medidor,año,mes)).
- Filtros avanzados y ordenamiento en tablas.
- Tests unitarios para cálculo de boletas y serialización de repositorios.
- Exportación/compartir PDF desde Web (descarga) y Desktop (ruta de guardado elegible).

---