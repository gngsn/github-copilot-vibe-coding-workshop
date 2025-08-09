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
  <div className="min-h-screen bg-[#F5F6FA] flex flex-col items-center justify-start font-sans">
  <div className="w-full max-w-[1280px] mt-[8px] flex flex-col items-center justify-start gap-[8px]">
  <form onSubmit={handleSearch} className="flex gap-[6px] mb-[8px] w-full max-w-[760px]">
          <input
            type="text"s
            className="flex-1 border border-[#D9D9D9] rounded-[10px] px-6 py-4 text-[24px] font-normal focus:outline-none focus:ring-2 focus:ring-[#00B7FF] bg-[#F5F6FA] text-[#222] placeholder:text-[#878787] placeholder:font-normal"
            placeholder="Enter keywords to search..."
            value={query}
            onChange={e => setQuery(e.target.value)}
            aria-label="Search posts"
          />
          <button
            type="submit"
            className="bg-[#00B7FF] text-white px-10 py-3 rounded-[10px] font-semibold shadow-lg hover:bg-[#0099cc] transition text-[20px] focus:outline-none"
            disabled={loading}
          >
            {loading ? 'Searching...' : 'Search'}
          </button>
        </form>
  {error && <div className="bg-red-100 text-red-700 p-2 rounded mb-4 text-center w-full max-w-[760px] text-[20px] font-normal">{error}</div>}
  <div className="flex flex-col gap-[8px] w-full max-w-[760px] mx-auto">
          {results.length === 0 && !loading ? (
            <div className="text-center text-[#878787] text-[24px] font-normal">No results found.</div>
          ) : (
            results.map(post => (
              <div key={post.id} className="bg-white rounded-[20px] shadow-lg px-[8px] pt-[8px] pb-[6px] flex flex-col gap-[6px] border border-[#E5A000] w-full">
                <div className="flex items-center gap-[6px]">
                  <img src={`https://ui-avatars.com/api/?name=${post.username}&background=E5A000&color=fff&size=100`} alt={post.username} className="rounded-full w-[100px] h-[100px]" />
                  <span className="font-bold text-[#222] text-[32px] leading-tight">{post.username}</span>
                  <span className="ml-auto text-xs text-[#878787] font-normal">{new Date(post.createdAt).toLocaleString()}</span>
                </div>
                <div className="text-[32px] text-[#222] leading-tight font-normal text-left">{post.content}</div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default Search;
