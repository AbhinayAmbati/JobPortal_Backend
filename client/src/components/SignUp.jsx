import NavBar from "./NavBarOut";
import { Link } from 'react-router-dom';
import { useState } from 'react';
import { FaUser, FaEnvelope, FaEyeSlash, FaEye, FaLock } from 'react-icons/fa';
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const SignUp = () => {
  const [username, setUserName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const navigate = useNavigate();

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const toggleConfirmPasswordVisibility = () => {
    setShowConfirmPassword(!showConfirmPassword);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Basic validation
    if (password !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }

    const userData = {
      username,
      email,
      password,
    };

    try {
      const response = await axios.post('http://localhost:8080/api/user/signup', userData); 
      console.log('Registration successful:', response.data)
      alert('Registration successful. Please sign in.');
      navigate('/sign-in');
    } catch (error) {
      console.error('Registration failed:', error.response.data);
      alert('Registration failed. Please try again.');
    }
  };

  return (
    <>
      <NavBar />
      <div className="flex items-center justify-center min-h-screen bg-gray-100">
        <div className="bg-white shadow-lg rounded-lg p-8 w-96">
          <h1 className="text-3xl font-bold text-center mb-6">Register</h1>
          <form className="space-y-4" onSubmit={handleSubmit}>
            <div className="relative">
              <label htmlFor="name" className="block text-sm font-medium text-gray-700">Name</label>
              <div className="flex items-center">
                <FaUser className="absolute left-3 top-10 text-gray-600" />
                <input 
                  type="text" 
                  id="name" 
                  value={username}
                  onChange={(e) => setUserName(e.target.value)}
                  className="mt-1 pl-10 p-2 border border-gray-300 rounded w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
            </div>
            <div className="relative">
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">Email</label>
              <div className="flex items-center">
                <FaEnvelope className="absolute left-3 top-10 text-gray-600" />
                <input 
                  type="email" 
                  id="email" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="mt-1 pl-10 p-2 border border-gray-300 rounded w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
            </div>
            <div className="relative">
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">Password</label>
              <div className="flex items-center">
                <FaLock className="absolute left-3 top-10 text-gray-600" />
                <input 
                  type={showPassword ? 'text' : 'password'} 
                  id="password" 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="mt-1 pl-10 p-2 border border-gray-300 rounded w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
                <button 
                  type="button" 
                  onClick={togglePasswordVisibility} 
                  className="absolute inset-y-0 right-0 flex items-center pr-3"
                  style={{marginTop:'25px'}}
                >
                  {showPassword ? <FaEyeSlash className="text-gray-800" /> : <FaEye className="text-gray-800" />}
                </button>
              </div>
            </div>
            <div className="relative">
              <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700">Confirm Password</label>
              <div className="flex items-center">
                <FaLock className="absolute left-3 top-10 text-gray-600" />
                <input 
                  type={showConfirmPassword ? 'text' : 'password'} 
                  id="confirmPassword" 
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  className="mt-1 pl-10 p-2 border border-gray-300 rounded w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
                <button 
                  type="button" 
                  onClick={toggleConfirmPasswordVisibility} 
                  className="absolute inset-y-0 right-0 flex items-center pr-3"
                  style={{marginTop:'25px'}}
                >
                  {showConfirmPassword ? <FaEyeSlash className="text-gray-800" /> : <FaEye className="text-gray-800" />}
                </button>
              </div>
            </div>
            <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition duration-300">
              Register
            </button>
            <h3 className="text-center mt-4">
              Already have an account? 
              <Link to="/sign-in" className="text-blue-600 hover:underline"> Sign In</Link>
            </h3>
          </form>
        </div>
      </div>
    </>
  );
}

export default SignUp;
