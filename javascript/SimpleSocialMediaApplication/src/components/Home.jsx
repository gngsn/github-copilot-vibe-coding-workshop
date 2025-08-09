import { useEffect, useState } from 'react';
import IconCommentSquare from '../assets/icons/icon-comment-square.svg';
import IconHeart from '../assets/icons/icon-heart.svg';
import IconHome from '../assets/icons/icon-home.svg';
import IconMagnifyingGlass from '../assets/icons/icon-magnifying-glass.svg';
import IconPerson from '../assets/icons/icon-person.svg';
import IconPlus from '../assets/icons/icon-plus.svg';
import IconXLetter from '../assets/icons/icon-x-letter.svg';
import NameInputModal from './NameInputModal';
import PostModal from './PostModal';
import Search from './Search';

const API_BASE = 'http://localhost:8000';

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [apiError, setApiError] = useState(false);
  const [loading, setLoading] = useState(true);
  const [showNameModal, setShowNameModal] = useState(false);
  const [showPostModal, setShowPostModal] = useState(false);
  const [showSearchModal, setShowSearchModal] = useState(false);

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

  useEffect(() => {
    fetchPosts();
  }, []);

  const handleNameSubmit = () => {
    // You can handle username logic here (e.g., save to state or send to API)
    // For demo, just close modal
    setShowNameModal(false);
  };

  const handlePostRefresh = () => {
    fetchPosts();
  };

  return (
  <div className="min-h-screen w-full flex flex-row bg-[#F5F6FA] font-sans">
    {/* Sidebar Navigation - Figma fidelity */}
    <aside className="h-screen w-[110px] bg-[#E5A000] flex flex-col items-center justify-between py-10 rounded-r-[20px] shadow-lg" aria-label="Sidebar">
      <div className="flex flex-col items-center gap-12 mt-4">
        <img src={IconHome} alt="Home" className="w-[58px] h-[51px]" tabIndex={0} aria-label="Home" />
        <img src={IconMagnifyingGlass} alt="Search" className="w-[58px] h-[58px] cursor-pointer" tabIndex={0} aria-label="Search" onClick={() => setShowSearchModal(true)} onKeyDown={e => (e.key === 'Enter' || e.key === ' ') && setShowSearchModal(true)} />
        <img src={IconPerson} alt="Profile" className="w-[58px] h-[58px] cursor-pointer" tabIndex={0} aria-label="Profile" onClick={() => setShowNameModal(true)} onKeyDown={e => (e.key === 'Enter' || e.key === ' ') && setShowNameModal(true)} />
        <img src={IconXLetter} alt="Close" className="w-[58px] h-[58px]" tabIndex={0} aria-label="Close" />
      </div>
    </aside>

    {/* Main Content */}
    <div className="flex-1 flex flex-col items-center justify-start gap-8">
      {/* Header - Figma fidelity */}
      <header className="w-full max-w-[1280px] flex items-center justify-between py-6 px-10 bg-[#E5A000] rounded-b-[20px] mb-8 shadow-lg" aria-label="App header">
        <div className="flex items-center gap-[4px]">
          <img src="https://ui-avatars.com/api/?name=Community&background=E5A000&color=fff&size=58" alt="Community" className="rounded-full w-[58px] h-[58px]" />
          <div>
            <h1 className="text-[32px] font-bold text-[#222] leading-tight">Community</h1>
            <p className="text-[20px] text-[#878787] leading-tight mb-4">Simple Social Media Application</p>
          </div>
        </div>
        <button className="flex items-center gap-2 bg-[#00B7FF] text-white px-8 py-2 rounded-[10px] font-semibold shadow-lg hover:bg-[#0099cc] transition text-[20px] focus:outline-none mb-6" aria-label="New Post" tabIndex={0} onClick={() => setShowPostModal(true)} onKeyDown={e => (e.key === 'Enter' || e.key === ' ') && setShowPostModal(true)}>
          <img src={IconPlus} alt="New Post" className="w-[24px] h-[24px]" />
          New Post
        </button>
      </header>

      {/* API error indication */}
      {apiError && (
        <div className="bg-red-100 text-red-700 p-4 mt-4 rounded-[10px] w-full max-w-[1280px] text-center text-[20px]">
          Backend API is unavailable or unreachable.
        </div>
      )}

      {/* Posts List */}
      <main className="w-full max-w-[1280px] px-10 flex flex-col items-center justify-start gap-8">
        {loading && !apiError && (
          <div className="mt-4 text-center text-[#878787] text-[24px]">Loading posts...</div>
        )}
        {!loading && !apiError && (
          <div className="mt-4 flex flex-col gap-8 items-center justify-start">
            {posts.length === 0 ? (
              <div className="text-center text-[#878787] text-[24px]">No posts found.</div>
            ) : (
              posts.map(post => (
                <div key={post.id} className="rounded-[20px] shadow-lg px-8 pt-8 pb-[6px] flex flex-col gap-6 border border-[#E5A000] bg-[#E5A000] w-full max-w-[760px] mx-auto">
                  <div className="flex items-center gap-[6px]">
                    <img src={`https://ui-avatars.com/api/?name=${post.username}&background=E5A000&color=fff&size=100`} alt={post.username} className="rounded-full w-[100px] h-[100px]" />
                    <span className="font-bold text-[#222] text-[32px] leading-tight">{post.username}</span>
                    <span className="ml-auto text-xs text-[#878787]">{new Date(post.createdAt).toLocaleString()}</span>
                  </div>
                  <div className="text-[32px] text-[#222] leading-tight mb-[4px]">{post.content}</div>
                  <div className="flex gap-[8px] mt-[2px]">
                    <span className="flex items-center gap-[2px] text-[#878787] text-[24px]">
                      <img src={IconHeart} alt="Likes" className="w-[58px] h-[44px]" />
                      {post.likes}
                    </span>
                    <span className="flex items-center gap-[2px] text-[#878787] text-[24px]">
                      <img src={IconCommentSquare} alt="Comments" className="w-[55px] h-[48px]" />
                      {post.comments}
                    </span>
                  </div>
                </div>
              ))
            )}
          </div>
        )}
      </main>
      {/* Modals */}
      {showNameModal && (
        <NameInputModal onClose={() => setShowNameModal(false)} onSubmit={handleNameSubmit} />
      )}
      {showPostModal && (
        <PostModal onClose={() => setShowPostModal(false)} onRefresh={handlePostRefresh} />
      )}
      {showSearchModal && (
        <Search onClose={() => setShowSearchModal(false)} />
      )}
    </div>
  </div>
  );
}

export default Home;

