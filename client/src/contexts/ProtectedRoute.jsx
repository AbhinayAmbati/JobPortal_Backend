import  { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import AppContext from './AppContext'; 

// eslint-disable-next-line react/prop-types
const ProtectedRoute = ({ children }) => {
  const { user } = useContext(AppContext);

  return user ? children : <Navigate to="/sign-in" replace />;
};

export default ProtectedRoute;