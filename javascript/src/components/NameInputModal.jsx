import { useState } from 'react';

const NameInputModal = ({ onClose, onSubmit }) => {
  const [username, setUsername] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (!username) {
      setError('Username is required.');
      return;
    }
    onSubmit(username);
    setUsername('');
    onClose();
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
        <h2 className="text-xl font-bold mb-4 text-[#222]">Enter Your Name</h2>
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
          {error && <div className="text-red-500 text-sm">{error}</div>}
          <div className="flex gap-3 mt-2">
            <button
              type="submit"
              className="bg-[#0D8ABC] text-white px-4 py-2 rounded-lg font-semibold shadow hover:bg-[#0971a3] transition flex-1"
            >
              Confirm
            </button>
            <button
              type="button"
              className="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg font-semibold flex-1"
              onClick={onClose}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default NameInputModal;
