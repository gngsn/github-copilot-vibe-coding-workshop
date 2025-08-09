import { useState } from 'react';

const API_BASE = 'http://localhost:8000';

const PostModal = ({ onClose, onRefresh }) => {
  const [username, setUsername] = useState('');
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    if (!username || !content) {
      setError('Username and content are required.');
      setLoading(false);
      return;
    }

    try {
      const response = await fetch(`${API_BASE}/posts`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: username.trim(),
          content: content.trim(),
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to create post');
      }

      await response.json();
      
      // Clear form and close modal
      setUsername('');
      setContent('');
      onClose();
      
      // Refresh posts list if callback provided
      if (onRefresh) {
        onRefresh();
      }
    } catch {
      setError('Failed to create post. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black font-sans">
      <div className="bg-[white] rounded-[20px] shadow-lg w-full max-w-[538px] px-[32px] pt-[32px] pb-[24px] relative flex flex-col min-h-[400px]">
        <button
          className="top-[32px] right-[32px] w-[40px] h-[40px] flex items-center justify-center text-[#00B7FF] text-[32px] font-bold rounded-[10px] hover:bg-[#CCF1FF] focus:bg-[#CCF1FF] focus:outline-none transition-colors"
          aria-label="Close modal"
          onClick={onClose}
          tabIndex={0}
          onKeyDown={e => (e.key === 'Enter' || e.key === ' ') && onClose()}
        >
          &times;
        </button>
        
        <h2 className="text-[36px] font-normal mb-[28px] text-black text-left leading-[1.2]">How do you feel today?</h2>
        
        <form onSubmit={handleSubmit} className="flex flex-col flex-1">
          <div className="flex flex-col gap-[20px] mb-[20px]">
            <input
              type="text"
              className="border-none bg-[#D9D9D9] rounded-[10px] px-[24px] py-[16px] text-[24px] text-[#222] focus:outline-none focus:ring-2 focus:ring-[#00B7FF] placeholder:text-[#878787] placeholder:font-normal w-full"
              placeholder="Your name"
              value={username}
              onChange={e => setUsername(e.target.value)}
              aria-label="Username"
              required
              disabled={loading}
            />
            <textarea
              className="border-none bg-[#D9D9D9] rounded-[10px] px-[24px] py-[16px] text-[24px] text-[#222] focus:outline-none focus:ring-2 focus:ring-[#00B7FF] resize-none placeholder:text-[#878787] placeholder:font-normal w-full min-h-[120px]"
              placeholder="What's on your mind?"
              value={content}
              onChange={e => setContent(e.target.value)}
              aria-label="Content"
              rows={4}
              required
              disabled={loading}
            />
          </div>
          
          {error && (
            <div className="text-red-500 text-[16px] mb-[20px] text-center">
              {error}
            </div>
          )}
          
          <div className="flex gap-[16px] mt-auto">
            <button
              type="submit"
              className="bg-[#00B7FF] text-white px-[32px] py-[12px] rounded-[10px] font-semibold shadow hover:bg-[#0099cc] transition flex-1 text-[20px] disabled:opacity-50 disabled:cursor-not-allowed"
              disabled={loading}
            >
              {loading ? 'Posting...' : 'Submit'}
            </button>
            <button
              type="button"
              className="bg-[#CCF1FF] text-[#00B7FF] px-[32px] py-[12px] rounded-[10px] font-semibold flex-1 text-[20px] hover:bg-[#B3E6FF] transition disabled:opacity-50 disabled:cursor-not-allowed"
              onClick={onClose}
              disabled={loading}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PostModal;
