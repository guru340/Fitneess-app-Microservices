import { BrowserRouter } from 'react-router-dom';
import { useContext, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { Button } from './components/ui/button';
import { setCredentials } from './store/authSlice';
import { Routes, Route, Navigate } from 'react-router-dom';
import ActivityForm from './pages/ActivityForm';
import ActivityList from './pages/ActivityList';
import ActivityDetail from './pages/ActivityDetail';
import { AuthContext } from 'react-oauth2-code-pkce';
import { Box } from '@mui/material';

const ActivitesPage=()=>{
  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
      <ActivityForm/>
      <ActivityList/>
    </Box>
  )
}

function App() {
  const{token,tokenData,logIn,logOut}=useContext(AuthContext);
  const dispatch = useDispatch();

  useEffect(() => {
    if(token ){
      dispatch(setCredentials({token,user:tokenData}));
    }
  }, 
  [token ,tokenData,dispatch]);

  return (
    <BrowserRouter>
    {!token ?(
      <Button variant='contained' color='primary'
        onClick={()=>{logIn()}}>
        LOGIN
      </Button>):(
        <div>
           <Button variant="outline"
            onClick={()=>{logOut()}}>
            LOGOUT
          </Button>
          <Routes >
            <Route path='/activities' element={<ActivitesPage/>}/>
            <Route path='/activities/:id' element={<ActivityDetail/>}/>
            <Route path='/' element={token ?<Navigate to="/activities" replace/> : <div>Please login</div>}/>
            
          </Routes>
        </div>
      )}
    </BrowserRouter>
  );
}

export default App;
