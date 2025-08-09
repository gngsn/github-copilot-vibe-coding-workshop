
import { useEffect, useState } from 'react';
import { Link, Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import Home from './components/Home';
import NameInputModal from './components/NameInputModal';
import PostDetails from './components/PostDetails';
import PostModal from './components/PostModal';
import Search from './components/Search';

const API_BASE = 'http://localhost:8000';

const App = () => {
  const [apiAvailable, setApiAvailable] = useState(true);

  useEffect(() => {
    const checkApi = async () => {
      try {
        const res = await fetch(`${API_BASE}/openapi.yaml`);
        setApiAvailable(res.ok);
      } catch {
        setApiAvailable(false);
      }
    };
    checkApi();
  }, []);

  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <nav className="bg-white shadow p-4 flex gap-4">
          <Link to="/" className="font-bold">Home</Link>
          <Link to="/search">Search</Link>
        </nav>
        {!apiAvailable && (
          <div className="bg-red-100 text-red-700 p-4 text-center">
            Backend API is unavailable or unreachable.
          </div>
        )}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/search" element={<Search />} />
          <Route path="/post/:postId" element={<PostDetails />} />
          <Route path="/post-modal" element={<PostModal />} />
          <Route path="/name-input-modal" element={<NameInputModal />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
