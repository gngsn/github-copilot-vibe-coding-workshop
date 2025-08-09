import { useState } from 'react';

const Search = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSearch = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await fetch('http://localhost:8000/posts');
      if (!res.ok) throw new Error('API unavailable');
      const data = await res.json();
      setResults(data.filter(post => post.content.toLowerCase().includes(query.toLowerCase())));
    } catch {
      setError('Failed to fetch posts.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#F5F6FA] flex flex-col items-center">
      <div className="w-full max-w-xl mt-8">
        <form onSubmit={handleSearch} className="flex gap-2 mb-6">
          <input
            type="text"
            className="flex-1 border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-[#0D8ABC]"
            placeholder="Search posts..."
            value={query}
            onChange={e => setQuery(e.target.value)}
            aria-label="Search posts"
          />
          <button
            type="submit"
            className="bg-[#0D8ABC] text-white px-4 py-2 rounded-lg font-semibold shadow hover:bg-[#0971a3] transition"
            disabled={loading}
          >
            {loading ? 'Searching...' : 'Search'}
          </button>
        </form>
        {error && <div className="bg-red-100 text-red-700 p-2 rounded mb-4 text-center">{error}</div>}
        <div className="flex flex-col gap-6">
          {results.length === 0 && !loading ? (
            <div className="text-center text-gray-400">No results found.</div>
          ) : (
            results.map(post => (
              <div key={post.id} className="bg-white rounded-2xl shadow p-6 flex flex-col gap-2">
                <div className="flex items-center gap-3">
                  <img src={`https://ui-avatars.com/api/?name=${post.username}&background=0D8ABC&color=fff&size=32`} alt={post.username} className="rounded-full w-8 h-8" />
                  <span className="font-semibold text-[#222]">{post.username}</span>
                  <span className="ml-auto text-xs text-gray-400">{new Date(post.createdAt).toLocaleString()}</span>
                </div>
                <div className="text-base text-[#222] mt-2">{post.content}</div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default Search;
