import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import AppContext from './AppContext'; 

// eslint-disable-next-line react/prop-types
const ProtectedLoginRoute = ({ children }) => {
  const { user } = useContext(AppContext);

  return user ? <Navigate to="/" replace /> : children;  
};

export default ProtectedLoginRoute;