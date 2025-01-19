import { FaSearch, FaBriefcase} from 'react-icons/fa';
import NavBar from './NavBarOut';

const Home = () => {
  return (
    <>
    <NavBar/>
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="text-center mb-10">
        <h1 className="text-5xl font-bold text-blue-600 mb-4">Welcome to the Job Finder Portal</h1>
        <p className="text-lg text-gray-700 mb-6">Find your dream job with ease. Search, apply, and get hired!</p>
        <div className="flex justify-center mb-4">
          <input 
            type="text" 
            placeholder="Search for jobs..." 
            className="p-3 border border-gray-300 rounded-l w-1/2"
          />
          <button className="bg-blue-600 text-white px-4 py-2 rounded-r hover:bg-blue-700 transition duration-300">
            <FaSearch />
          </button>
        </div>
      </div>
      <h2 className="text-3xl font-semibold mb-4">Job Listings</h2>
      <ul className="bg-white shadow-md rounded-lg p-4 w-1/2">
        <li className="border-b py-2 flex items-center">
          <FaBriefcase className="mr-2 text-blue-600" /> Job Title 1
        </li>
        <li className="border-b py-2 flex items-center">
          <FaBriefcase className="mr-2 text-blue-600" /> Job Title 2
        </li>
        <li className="border-b py-2 flex items-center">
          <FaBriefcase className="mr-2 text-blue-600" /> Job Title 3
        </li>
      </ul>
      <div className="mt-6">
        <h3 className="text-2xl font-semibold mb-2">Why Choose Us?</h3>
        <p className="text-gray-700 mb-4">
          We connect you with top employers and provide resources to help you succeed in your job search.
        </p>
      </div>
    </div></>
  )
}

export default Home;
