import NavBar from './NavBar';
import { FaBriefcase } from 'react-icons/fa';
import { useAppContext } from '../contexts/AppContext';

const Home = () => {

    const { setUser } = useAppContext();
 
    setUser(true);

  return (
    <>
      <NavBar  />
      <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
        <h1 className="text-5xl font-bold text-blue-600 mb-4">Welcome to the Job Finder Portal</h1>
        <p className="text-lg text-gray-700 mb-6">Find your dream job with ease. Search, apply, and get hired!</p>
        <h2 className="text-3xl font-semibold mb-4">Job Listings</h2>
        <div className="bg-white shadow-md rounded-lg p-4 w-1/2">
          <div className="border-b py-2 flex items-center">
            <FaBriefcase className="mr-2 text-blue-600" /> Job Title 1
          </div>
          <div className="border-b py-2 flex items-center">
            <FaBriefcase className="mr-2 text-blue-600" /> Job Title 2
          </div>
          <div className="border-b py-2 flex items-center">
            <FaBriefcase className="mr-2 text-blue-600" /> Job Title 3
          </div>
        </div>
        <div className="mt-6">
          <h3 className="text-2xl font-semibold mb-2">Why Choose Us?</h3>
          <p className="text-gray-700 mb-4">
            We connect you with top employers and provide resources to help you succeed in your job search.
          </p>
        </div>
      </div>
    </>
  );
}

export default Home;
