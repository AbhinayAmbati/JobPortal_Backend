import logo from '../assets/logo.svg';
import { Link } from 'react-router-dom';
import { FaUserLock, FaUserPlus } from 'react-icons/fa';


const NavBar = () => {
  return (
    <div className="flex fixed w-full items-center justify-between p-4 bg-blue-600 text-white shadow-md">
      <div className="flex items-center">
        <Link to="/job-portal"> <img src={logo} alt="Job Portal Logo" className="h-10 w-30 mr-2" /></Link>
        <h1 className="text-xl font-semibold">Job Portal</h1>
      </div>
      <div className="space-x-4 flex">
        <Link to='/sign-up'><button className="hover:bg-blue-500 px-3 py-2  items-center gap-1 rounded flex transition duration-300"><FaUserPlus/>Sign Up</button></Link>
        <Link to='/sign-in'><button className="hover:bg-blue-500 px-3 py-2 items-center gap-1  rounded flex transition duration-300"><FaUserLock/>Sign In</button></Link>
      </div>
    </div>
  )
}

export default NavBar;
