import logo from '../assets/logo.svg';
import { Link } from 'react-router-dom';
import { FaHome, FaBriefcase, FaUser, FaSignOutAlt } from 'react-icons/fa';
import Cookies from 'js-cookie';

const NavBar = () => {
  
  const username = Cookies.get('user');

  const handleLogout = () => {
    try {

      Cookies.remove('username');
      Cookies.remove('token');
      
      
      window.location.href = '/sign-in';
    } catch (error) {
      console.error('Logout error:', error);
    }
  };
  

  return (
    <div className="flex fixed w-full items-center justify-between p-4 bg-blue-600 text-white shadow-md">
      <div className="flex items-center">
        <Link to="/"><img src={logo} alt="Job Portal Logo" className="h-10 w-30 mr-2" /></Link>
        <h1 className="text-xl font-semibold">Job Portal</h1>
      </div>
      <div className="space-x-4 flex items-center">
        <Link to='/'><button className="hover:bg-blue-500 px-3 py-2 flex rounded items-center gap-1 transition duration-300"><FaHome /> Home</button></Link>
        <Link to='/jobs'><button className="hover:bg-blue-500 px-3 py-2 flex items-center gap-1 rounded transition duration-300"><FaBriefcase /> Jobs</button></Link>
        <Link to='/companies'><button className="hover:bg-blue-500 px-3 py-2 items-center gap-1 flex rounded transition duration-300"><FaUser /> Companies</button></Link>
        <div className="flex items-center">
          <span className="mr-2">{username}</span>
          <button onClick={handleLogout} className="hover:bg-blue-500 px-3 py-2 items-center gap-1 flex rounded transition duration-300"><FaSignOutAlt /> Logout</button>
        </div>
      </div>
    </div>
  );
}

export default NavBar;
