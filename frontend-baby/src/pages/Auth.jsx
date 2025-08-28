// src/pages/Auth.jsx
import React, { useState } from "react";
import {
  Box, Grid, Paper, Tabs, Tab, Typography, TextField, Button,
  Checkbox, FormControlLabel, Link, InputAdornment
} from "@mui/material";
import RocketLaunchOutlinedIcon from "@mui/icons-material/RocketLaunchOutlined";
import EmailOutlinedIcon from "@mui/icons-material/EmailOutlined";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import { useNavigate } from "react-router-dom";

function Auth() {
  const [mode, setMode] = useState("signup");
  const [remember, setRemember] = useState(true);
  const [form, setForm] = useState({ fullName: "", email: "", password: "", accept: true });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  function handleTab(_, v) { setMode(v); }
  function onChange(e) {
    var name = e.target.name;
    var value = e.target.type === "checkbox" ? e.target.checked : e.target.value;
    setForm(function(p){ var c={}; for (var k in p) c[k]=p[k]; c[name]=value; return c; });
  }
  function onSubmit(e) {
    e.preventDefault();
    localStorage.setItem("jwt", "token_demo");
    navigate("/dashboard");
  }

  var darkFieldSx = {
    "& .MuiInputBase-input": { color: "white" },
    "& .MuiInputLabel-root": { color: "rgba(255,255,255,0.7)" },
    "& .MuiOutlinedInput-notchedOutline": { borderColor: "rgba(255,255,255,0.2)" },
    "&:hover .MuiOutlinedInput-notchedOutline": { borderColor: "rgba(255,255,255,0.35)" }
  };

  return (
    <Box sx={{ minHeight: "100vh", display: "grid", placeItems: "center", px: 2 }}>
      <Paper elevation={12}
        sx={{ width: { xs: "100%", sm: 980 }, height: { xs: "auto", sm: 560 }, borderRadius: 4, overflow: "hidden" }}>
        {/* ¡IMPORTANTE! El container tiene altura completa */}
        <Grid container sx={{ height: "100%" }}>
          <Grid item xs={12} sm={6}>
            {/* Fondo y layout en un Box con height: 100% */}
            <Box sx={{
              height: "100%", p: 5, color: "white",
              display: "flex", alignItems: "center", justifyContent: "center",
              background: "linear-gradient(145deg,#41d3bd 0%,#2bb7a3 50%,#1fa293 100%)",
              position: "relative"
            }}>
              <Box sx={{ position: "absolute", top: 20, left: 24, opacity: 0.9 }}>
                <RocketLaunchOutlinedIcon fontSize="large" />
              </Box>

              <Box sx={{ maxWidth: 380 }}>
                <Typography variant="h4" fontWeight={700} gutterBottom>
                  Plan your activities and control your progress online
                </Typography>
                <Typography sx={{ opacity: 0.95 }}>
                  BabyTrackMaster te ayuda a registrar cuidados, citas y rutinas del bebé,
                  y a ver estadísticas rápidas.
                </Typography>

                <Box sx={{ mt: 6, border: "2px dashed rgba(255,255,255,.6)", borderRadius: 3, p: 4 }}>
                  <Box component="svg" viewBox="0 0 200 120" sx={{ width: "100%", height: 120 }}>
                    <g fill="none" stroke="white" strokeWidth="2" opacity="0.9">
                      <path d="M100 20 L120 60 L100 50 L80 60 Z" />
                      <path d="M100 50 L100 90" />
                      <circle cx="100" cy="45" r="6" />
                      <path d="M90 90 C95 100,105 100,110 90" />
                      <path d="M70 100 Q100 115 130 100" />
                      <path d="M60 108 Q100 123 140 108" />
                    </g>
                  </Box>
                </Box>
              </Box>
            </Box>
          </Grid>

          <Grid item xs={12} sm={6}>
            {/* Fondo oscuro aplicado aquí y altura completa */}
            <Box sx={{
              height: "100%", p: { xs: 3, sm: 6 },
              display: "flex", flexDirection: "column",
              background: "linear-gradient(145deg,#121826 0%,#1a2234 100%)",
              color: "rgba(255,255,255,0.92)"
            }}>
              <Box sx={{ display: "flex", justifyContent: "space-between", mb: 1 }}>
                <Typography variant="h5" fontWeight={700}>
                  {mode === "signin" ? "Sign In" : "Sign Up"}
                </Typography>
                <Button size="small" variant="outlined"
                  onClick={function(){ setMode("signup"); }}
                  sx={{ borderColor: "rgba(255,255,255,0.2)", color: "rgba(255,255,255,0.9)", display: { xs: "none", sm: "inline-flex" } }}>
                  Sign Up
                </Button>
              </Box>

              <Tabs value={mode} onChange={handleTab} textColor="inherit"
                TabIndicatorProps={{ style: { backgroundColor: "#41d3bd" } }}
                sx={{ "& .MuiTab-root": { textTransform: "none" }, mb: 3 }}>
                <Tab value="signin" label="Sign In" />
                <Tab value="signup" label="Sign Up" />
              </Tabs>

              <Box component="form" onSubmit={onSubmit} noValidate>
                {mode === "signup" && (
                  <TextField fullWidth margin="normal" name="fullName" label="Full name"
                    value={form.fullName} onChange={onChange}
                    InputProps={{ startAdornment: <InputAdornment position="start"><PersonOutlineOutlinedIcon sx={{ color: "rgba(255,255,255,0.7)" }}/></InputAdornment> }}
                    sx={darkFieldSx}/>
                )}

                <TextField fullWidth margin="normal" name="email" label="E-mail" type="email"
                  value={form.email} onChange={onChange}
                  InputProps={{ startAdornment: <InputAdornment position="start"><EmailOutlinedIcon sx={{ color: "rgba(255,255,255,0.7)" }}/></InputAdornment> }}
                  sx={darkFieldSx}/>

                <TextField fullWidth margin="normal" name="password" label="Password" type="password"
                  value={form.password} onChange={onChange}
                  InputProps={{ startAdornment: <InputAdornment position="start"><LockOutlinedIcon sx={{ color: "rgba(255,255,255,0.7)" }}/></InputAdornment> }}
                  sx={darkFieldSx}/>

                {mode === "signup" ? (
                  <FormControlLabel
                    sx={{ mt: 1, mb: 1 }}
                    control={<Checkbox name="accept" checked={form.accept} onChange={onChange} sx={{ color: "rgba(255,255,255,0.7)" }} />}
                    label={<Typography variant="body2" sx={{ color: "rgba(255,255,255,0.8)" }}>
                      I agree all statements in <Link href="#" underline="hover" sx={{ color: "#41d3bd" }}>terms of service</Link>
                    </Typography>}
                  />
                ) : (
                  <FormControlLabel
                    sx={{ mt: 1, mb: 1 }}
                    control={<Checkbox checked={remember} onChange={function(e){ setRemember(e.target.checked); }} sx={{ color: "rgba(255,255,255,0.7)" }} />}
                    label={<Typography variant="body2" sx={{ color: "rgba(255,255,255,0.8)" }}>Remember me</Typography>}
                  />
                )}

                <Button type="submit" fullWidth size="large" variant="contained"
                  disabled={loading || (mode === "signup" && !form.accept)}
                  sx={{
                    mt: 2, py: 1.2, fontWeight: 700, borderRadius: 3,
                    background: "linear-gradient(145deg,#41d3bd,#2bb7a3)",
                    boxShadow: "0 8px 22px rgba(65,211,189,0.35)",
                    "&:hover": { filter: "brightness(0.95)" }
                  }}>
                  {mode === "signin" ? "Sign In" : "Sign Up"}
                </Button>

                <Box sx={{ mt: 2, display: "flex", justifyContent: "space-between", alignItems: "center", gap: 2 }}>
                  {mode === "signin" ? (
                    <Typography variant="body2" sx={{ color: "rgba(255,255,255,0.8)" }}>
                      ¿No tienes cuenta?{" "}
                      <Link component="button" onClick={function(){ setMode("signup"); }} sx={{ color: "#41d3bd" }}>
                        Regístrate
                      </Link>
                    </Typography>
                  ) : (
                    <Typography variant="body2" sx={{ color: "rgba(255,255,255,0.8)" }}>
                      ¿Ya tienes cuenta?{" "}
                      <Link component="button" onClick={function(){ setMode("signin"); }} sx={{ color: "#41d3bd" }}>
                        Inicia sesión
                      </Link>
                    </Typography>
                  )}

                  <Link href="#" underline="hover" sx={{ color: "#9adbd0" }}>
                    ¿Olvidaste tu contraseña?
                  </Link>
                </Box>
              </Box>
            </Box>
          </Grid>
        </Grid>
      </Paper>
    </Box>
  );
}

export default Auth;
