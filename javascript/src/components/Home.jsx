import { useEffect, useState } from 'react';
const API_BASE = 'http://localhost:8000';

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [apiError, setApiError] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPosts = async () => {
      setLoading(true);
      try {
        const res = await fetch(`${API_BASE}/posts`);
        if (!res.ok) throw new Error('API unavailable');
        const data = await res.json();
        setPosts(data);
        setApiError(false);
      } catch {
        setApiError(true);
      } finally {
        setLoading(false);
      }
    };
    fetchPosts();
  }, []);

  return (
    <div className="min-h-screen bg-[#F5F6FA] flex flex-col items-center">
      {/* Header */}
      <header className="w-full max-w-xl flex items-center justify-between py-6 px-4 bg-white shadow-sm rounded-b-2xl mb-6">
        <div className="flex items-center gap-3">
          <img src="https://ui-avatars.com/api/?name=Community&background=0D8ABC&color=fff&size=48" alt="Community" className="rounded-full w-12 h-12" />
          <div>
            <h1 className="text-2xl font-bold text-[#222]">Community</h1>
            <p className="text-sm text-gray-500">Simple Social Media Application</p>
          </div>
        </div>
        <button className="bg-[#0D8ABC] text-white px-4 py-2 rounded-lg font-semibold shadow hover:bg-[#0971a3] transition">New Post</button>
      </header>

      {/* API error indication */}
      {apiError && (
        <div className="bg-red-100 text-red-700 p-4 mt-4 rounded w-full max-w-xl text-center">
          Backend API is unavailable or unreachable.
        </div>
      )}

      {/* Posts List */}
      <main className="w-full max-w-xl px-4">
        {loading && !apiError && (
          <div className="mt-4 text-center text-gray-500">Loading posts...</div>
        )}
        {!loading && !apiError && (
          <div className="mt-4 flex flex-col gap-6">
            {posts.length === 0 ? (
              <div className="text-center text-gray-400">No posts found.</div>
            ) : (
              posts.map(post => (
                <div key={post.id} className="bg-white rounded-2xl shadow p-6 flex flex-col gap-2">
                  <div className="flex items-center gap-3">
                    <img src={`https://ui-avatars.com/api/?name=${post.username}&background=0D8ABC&color=fff&size=32`} alt={post.username} className="rounded-full w-8 h-8" />
                    <span className="font-semibold text-[#222]">{post.username}</span>
                    <span className="ml-auto text-xs text-gray-400">{new Date(post.createdAt).toLocaleString()}</span>
                  </div>
                  <div className="text-base text-[#222] mt-2">{post.content}</div>
                  <div className="flex gap-6 mt-2">
                    <span className="flex items-center gap-1 text-gray-500"><svg width="18" height="18" fill="none" viewBox="0 0 24 24"><path d="M7 10v4a5 5 0 0 0 10 0v-4" stroke="#0D8ABC" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/><path d="M12 19v2" stroke="#0D8ABC" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg> {post.likes}</span>
                    <span className="flex items-center gap-1 text-gray-500"><svg width="18" height="18" fill="none" viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" stroke="#0D8ABC" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg> {post.comments}</span>
                  </div>
                </div>
              ))
            )}
          </div>
        )}
      </main>
    </div>
  );
};

export default Home;
