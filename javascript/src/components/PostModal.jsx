import { useState } from 'react';

const PostModal = ({ onClose, onSubmit }) => {
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
      await onSubmit({ username, content });
      setUsername('');
      setContent('');
      onClose();
    } catch (err) {
      setError('Failed to create post.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
      <div className="bg-white rounded-2xl shadow-lg w-full max-w-md p-8 relative">
        <button
          className="absolute top-4 right-4 text-gray-400 hover:text-gray-600 text-xl"
          aria-label="Close modal"
          onClick={onClose}
        >
          &times;
        </button>
        <h2 className="text-xl font-bold mb-4 text-[#222]">Create New Post</h2>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input
            type="text"
            className="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-[#0D8ABC]"
            placeholder="Your name"
            value={username}
            onChange={e => setUsername(e.target.value)}
            aria-label="Username"
            required
          />
          <textarea
            className="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-[#0D8ABC] resize-none"
            placeholder="What's on your mind?"
            value={content}
            onChange={e => setContent(e.target.value)}
            aria-label="Content"
            rows={4}
            required
          />
          {error && <div className="text-red-500 text-sm">{error}</div>}
          <div className="flex gap-3 mt-2">
            <button
              type="submit"
              className="bg-[#0D8ABC] text-white px-4 py-2 rounded-lg font-semibold shadow hover:bg-[#0971a3] transition flex-1"
              disabled={loading}
            >
              {loading ? 'Posting...' : 'Post'}
            </button>
            <button
              type="button"
              className="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg font-semibold flex-1"
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
