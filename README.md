# FichaJuego · Proyecto de clase de la UT2 (PMDM 26/27)

Proyecto que construimos **en clase, sesión a sesión**, durante la UT2: una ficha de videojuego hecha con Jetpack Compose.

Parte de la plantilla `ProyectoComposeBase` del curso (IntelliJ IDEA 2026.2, AGP 9.1.1, compileSdk 37). Los requisitos, la configuración del SDK y del JDK, y los errores frecuentes son los mismos que los de la plantilla: consulta su README en el curso.

## Cómo seguir las sesiones

Cada sesión tiene dos *tags*: el punto de partida (`-inicio`) y el resultado al terminar la clase (`-fin`).

| Sesión | Inicio | Fin |
| :--- | :--- | :--- |
| 2.01 · Primer contacto con Compose | `s2.01-inicio` | `s2.01-fin` |

```bash
git clone <url-del-repositorio> fichajuego
cd fichajuego
git checkout s2.01-inicio         # ver el punto de partida de la sesión
git switch -c mi-trabajo          # crea tu rama para trabajar sobre él
```

Si faltaste a clase, haz `git checkout s2.01-fin` para ver cómo quedó el proyecto y continúa desde ahí.

¿No usas Git? En GitHub, en *Tags*, cada tag se puede descargar como `.zip`.
