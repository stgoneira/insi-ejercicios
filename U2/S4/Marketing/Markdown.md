# Marketing

## Ejecución

```
# En Linux o Mac
./MarketingApp/bin/app

# En Windows
.\MarketingApp/bin/app.bat
```

## Cambio Puerto

Por defecto se ejecuta en el puerto 9052, para ejecutar en el puerto 8080 deberíamos hacer algo así:

```
./MarketingApp/bin/app 8080
```


## API de Contactos

Esta guía documenta los recursos disponibles en `/api/contactos`.
Formato de datos: **JSON**.

### 1. JSON
Este es el formato JSON que deben enviar para crear o editar.

```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@ejemplo.com",
  "telefono": "+56912345678",
  "empresa": "Empresa Demo",
  "origen": "web",
  "estado": "NUEVO",
  "notas": "Cliente interesado"
}
```

### 2. Endpoints 

| Acción | Método | URL | Descripción |
| :--- | :--- | :--- | :--- |
| **Listar** | `GET` | `/api/contactos` | Trae todos los contactos. |
| **Crear** | `POST` | `/api/contactos` | Crea uno nuevo (enviar JSON). |
| **Ver Uno** | `GET` | `/api/contactos/{id}` | Busca por ID. |
| **Editar** | `PUT` | `/api/contactos/{id}` | Reemplaza datos (enviar JSON completo). |
| **Borrar** | `DELETE`| `/api/contactos/{id}` | Elimina el contacto. |

