# Sistema Finanzas

## Ejecución

```
# En Linux o Mac
./FinanzasApp/bin/app

# En Windows
.\FinanzasApp/bin/app.bat
```

## Cambio Puerto

Por defecto se ejecuta en el puerto 9051, para ejecutar en el puerto 8080 deberíamos hacer algo así:

```
./FinanzasApp/bin/app 8080
```

## WSDL

http://localhost:9051/soap/finanzas?wsdl

## Métodos disponibles en el servicio `ServiciosContabilidadYFinanzas`

### 1. crearSocio
- **Descripción**: Permite registrar un nuevo socio en el sistema.
- **Parámetros de entrada**:
  - `id` *(string)*: Identificador del socio.
  - `nombre` *(string)*: Nombre del socio.
- **Respuesta**:
  - `socioId` *(string)*: Identificador único asignado al socio creado.


### 2. crearCuotaPorCobrar
- **Descripción**: Genera una nueva cuota por cobrar asociada a un socio.
- **Parámetros de entrada**:
  - `socioId` *(string)*: Identificador del socio al que se le asigna la cuota.
  - `monto` *(decimal)*: Valor de la cuota.
  - `vencimiento` *(anySimpleType)*: Fecha de vencimiento de la cuota (usar XMLGregorianCalendar).
- **Respuesta**:
  - `cuotaId` *(string)*: Identificador único de la cuota creada.
