# FichaJuego · Proyecto de clase de PMDM 26/27 (UT2 y UT3)

Proyecto que construimos **en clase, sesión a sesión**, durante la UT2 y la UT3: una biblioteca de videojuegos hecha con Jetpack Compose.

Parte de la plantilla `ProyectoComposeBase` del curso (IntelliJ IDEA 2026.2, AGP 9.1.1, compileSdk 37). Los requisitos, la configuración del SDK y del JDK, y los errores frecuentes son los mismos que los de la plantilla: consulta su README en el curso.

## Cómo seguir las sesiones

Cada sesión tiene dos *tags*: el punto de partida (`-inicio`) y el resultado al terminar la clase (`-fin`).

| Sesión | Inicio | Fin |
| :--- | :--- | :--- |
| 2.01 · Primer contacto con Compose | `s2.01-inicio` | `s2.01-fin` |
| 2.02 · Estado y state hoisting | `s2.02-inicio` | `s2.02-fin` |
| 2.03 · Imágenes, iconos y recursos (estudio autónomo) | `s2.03-inicio` | `s2.03-fin` |
| 3.01 · Tema de Material 3, Scaffold y listas | `s3.01-inicio` | `s3.01-fin` |
| 3.02 · Componentes interactivos | `s3.02-inicio` | `s3.02-fin` |
| 3.03 · Navigation 3 (I) | `s3.03-inicio` | `s3.03-fin` |
| 3.04 · Navigation 3 (II) y edge-to-edge | `s3.04-inicio` | `s3.04-fin` |

```bash
git clone <url-del-repositorio> fichajuego
cd fichajuego
git checkout s2.01-inicio         # ver el punto de partida de la sesión
git switch -c mi-trabajo          # crea tu rama para trabajar sobre él
```

Si faltaste a clase, haz `git checkout s2.01-fin` para ver cómo quedó el proyecto y continúa desde ahí.

¿No usas Git? En GitHub, en *Tags*, cada tag se puede descargar como `.zip`.

## Créditos

- Ilustraciones de portada (`res/drawable/img_portada_*.jpg`): originales del curso, con la misma licencia que los apuntes (CC BY-NC-SA). No son las carátulas oficiales de los juegos.
- Iconos (`res/drawable/ic_*.xml`): [Material Symbols](https://fonts.google.com/icons) de Google, licencia Apache 2.0.
