import React from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App'
import { store } from './store/store'
import { Provider } from 'react-redux'
import { AuthProvider } from 'react-oauth2-code-pkce'
import { authConfig } from './store/authConfig'

const container = document.getElementById('root')

if (container) {
  const root = createRoot(container)

  root.render(
    <AuthProvider authConfig={authConfig}>
      <Provider store={store}>
        <App />
      </Provider>
    </AuthProvider>
  )
} else {
  throw new Error(
    "Root element with ID 'root' was not found in the document. Ensure there is a corresponding HTML element with the ID 'root' in your HTML file.",
  )
}