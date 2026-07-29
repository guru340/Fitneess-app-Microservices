import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { useContext, useEffect, useState } from 'react'
import { useDispatch } from 'react-redux'
import { AuthContext } from 'react-oauth2-code-pkce'
import { Box, Button, Chip, Container, CssBaseline, ThemeProvider, Typography, createTheme } from '@mui/material'
import ActivityForm from './pages/ActivityForm'
import ActivityList from './pages/ActivityList'
import ActivityDetail from './pages/ActivityDetail'
import { setCredentials } from './store/authSlice'

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#0f766e',
      contrastText: '#ffffff',
    },
    secondary: {
      main: '#f97316',
    },
    background: {
      default: '#f7faf8',
      paper: '#ffffff',
    },
    text: {
      primary: '#12201c',
      secondary: '#60726b',
    },
  },
  shape: {
    borderRadius: 8,
  },
  typography: {
    fontFamily: '"Inter Variable", Inter, system-ui, sans-serif',
    h1: {
      fontWeight: 850,
      letterSpacing: 0,
    },
    h4: {
      fontWeight: 800,
      letterSpacing: 0,
    },
    h5: {
      fontWeight: 800,
      letterSpacing: 0,
    },
    h6: {
      fontWeight: 750,
      letterSpacing: 0,
    },
    button: {
      fontWeight: 750,
      textTransform: 'none',
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          boxShadow: 'none',
        },
        containedPrimary: {
          background: 'linear-gradient(135deg, #0f766e 0%, #16a34a 100%)',
          '&:hover': {
            boxShadow: '0 12px 28px rgba(15, 118, 110, 0.24)',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          boxShadow: '0 18px 50px rgba(18, 32, 28, 0.08)',
          border: '1px solid rgba(96, 114, 107, 0.14)',
        },
      },
    },
    MuiTextField: {
      defaultProps: {
        variant: 'outlined',
      },
    },
  },
})

const Dashboard = () => {
  const [refreshKey, setRefreshKey] = useState(0)

  return (
    <Container maxWidth="xl" sx={{ py: { xs: 3, md: 5 } }}>
      <Box className="dashboard-hero">
        <Box>
          <Chip label="AI fitness companion" className="hero-chip" />
          <Typography variant="h1" className="hero-title">
            Train smarter with FITTRACK.
          </Typography>
          <Typography className="hero-copy">
            Log every workout, review effort trends, and get AI-backed coaching cues after each session.
          </Typography>
        </Box>
        <Box className="hero-metrics" aria-label="FITTRACK highlights">
          <Box>
            <Typography className="metric-value">24/7</Typography>
            <Typography className="metric-label">Activity tracking</Typography>
          </Box>
          <Box>
            <Typography className="metric-value">AI</Typography>
            <Typography className="metric-label">Recovery insights</Typography>
          </Box>
          <Box>
            <Typography className="metric-value">3</Typography>
            <Typography className="metric-label">Workout modes</Typography>
          </Box>
        </Box>
      </Box>

      <Box className="dashboard-grid">
        <ActivityForm onActivityAdded={() => setRefreshKey((key) => key + 1)} />
        <ActivityList refreshKey={refreshKey} />
      </Box>
    </Container>
  )
}

const LoginScreen = ({ onLogin }) => (
  <Box className="login-screen">
    <Box className="login-panel">
      <Chip label="FITTRACK" className="brand-chip" />
      <Typography variant="h1" className="login-title">
        Your fitness command center.
      </Typography>
      <Typography className="login-copy">
        Track runs, walks, and rides with a cleaner dashboard built for daily progress.
      </Typography>
      <Button variant="contained" size="large" onClick={onLogin}>
        Sign in to FITTRACK
      </Button>
    </Box>
  </Box>
)

function App() {
  const { token, tokenData, logIn, logOut } = useContext(AuthContext)
  const dispatch = useDispatch()

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }))
    }
  }, [token, tokenData, dispatch])

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <BrowserRouter>
        {!token ? (
          <LoginScreen onLogin={logIn} />
        ) : (
          <Box className="app-shell">
            <Box component="header" className="topbar">
              <Box>
                <Typography className="brand-mark">FITTRACK</Typography>
                <Typography className="brand-subtitle">Microservices fitness dashboard</Typography>
              </Box>
              <Button variant="outlined" color="primary" onClick={logOut}>
                Log out
              </Button>
            </Box>
            <Routes>
              <Route path="/activities" element={<Dashboard />} />
              <Route path="/activities/:id" element={<ActivityDetail />} />
              <Route path="/" element={<Navigate to="/activities" replace />} />
            </Routes>
          </Box>
        )}
      </BrowserRouter>
    </ThemeProvider>
  )
}

export default App
