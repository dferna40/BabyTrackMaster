import React from "react";
import Box from "@mui/material/Box";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import Link from "@mui/material/Link";

export default function Acerca() {
  return (
    <Box sx={{ width: "100%", maxWidth: 800 }}>
      <Typography variant="h4" gutterBottom>
        Acerca de
      </Typography>

      <Stack spacing={3}>
        <div>
          <Typography variant="h6" gutterBottom>
            Presentación breve
          </Typography>
          <Typography>
            BabyTrackMaster es una aplicación web modular diseñada para ayudar a
            padres y familias a organizar y gestionar el cuidado diario de sus
            bebés de forma sencilla, segura y centralizada.
          </Typography>
        </div>

        <div>
          <Typography variant="h6" gutterBottom>
            Misión y propósito
          </Typography>
          <Typography>
            Nuestra misión es facilitar la gestión de rutinas, citas, cuidados y
            recuerdos del bebé, ahorrando tiempo a los padres, reduciendo
            olvidos y ofreciendo estadísticas y recordatorios inteligentes que
            apoyan el día a día familiar.
          </Typography>
        </div>

        <div>
          <Typography variant="h6" gutterBottom>
            Historia o motivación
          </Typography>
          <Typography>
            BabyTrackMaster surge de la necesidad de muchos padres primerizos de
            contar con una herramienta digital que centralice en un solo lugar
            todo lo relacionado con el cuidado de su bebé: desde el registro de
            biberones, pañales y rutinas, hasta hitos importantes y citas
            médicas. La motivación es clara: ofrecer tranquilidad y orden en una
            etapa de grandes cambios.
          </Typography>
        </div>

        <div>
          <Typography variant="h6" gutterBottom>
            Equipo o responsables
          </Typography>
          <Typography>
            Creador: Proyecto desarrollado por David (analista programador con
            experiencia en Java y desarrollo web).
          </Typography>
          <Typography>
            Tecnología: Aplicación construida con Spring Boot (backend), React
            (frontend) y MySQL (bases de datos por microservicio), desplegada
            con Docker y Jenkins.
          </Typography>
          <Typography>
            Valores clave del desarrollo: seguridad (uso de JWT y Spring
            Security), escalabilidad (arquitectura de microservicios),
            innovación (integración con notificaciones y estadísticas en tiempo
            real).
          </Typography>
        </div>

        <div>
          <Typography variant="h6" gutterBottom>
            Valores y filosofía
          </Typography>
          <Typography>
            Seguridad: protección de datos con autenticación JWT y cifrado.
          </Typography>
          <Typography>
            Innovación: arquitectura moderna con microservicios y tecnologías
            cloud.
          </Typography>
          <Typography>
            Accesibilidad: interfaz clara y fácil de usar con Material UI.
          </Typography>
          <Typography>
            Confianza: backup de datos, soporte offline y multicuenta para
            familias.
          </Typography>
        </div>

        <div>
          <Typography variant="h6" gutterBottom>
            Contacto / enlaces
          </Typography>
          <Typography>
            📧 Correo de soporte:{" "}
            <Link href="mailto:soporte@babytrackmaster.com">
              soporte@babytrackmaster.com
            </Link>
          </Typography>
          <Typography>🌐 Web: www.babytrackmaster.com</Typography>
          <Typography>
            📱 Redes sociales: Facebook / Instagram / Twitter (opcional)
          </Typography>
          <Button
            variant="contained"
            sx={{
              mt: 1,
              alignSelf: "flex-start",
              backgroundColor: "#0d6efd",
              "&:hover": { backgroundColor: "#0b5ed7" },
            }}
            href="mailto:soporte@babytrackmaster.com"
          >
            Contáctanos
          </Button>
        </div>
      </Stack>
    </Box>
  );
}
