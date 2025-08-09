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
  <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40 font-sans">
  <div className="bg-white rounded-[20px] shadow-lg w-full max-w-[538px] px-[8px] pt-[8px] pb-[6px] relative flex flex-col min-h-[261px]">
        <button
          className="absolute top-8 right-8 w-10 h-10 flex items-center justify-center text-gray-400 hover:text-gray-600 text-[32px] font-bold rounded-[10px] focus:outline-none"
          aria-label="Close modal"
          onClick={onClose}
          tabIndex={0}
          onKeyDown={e => (e.key === 'Enter' || e.key === ' ') && onClose()}
        >
          &times;
        </button>
  <h2 className="text-[36px] font-bold mb-[7px] text-black leading-[0.73]">Enter your username</h2>
  <form onSubmit={handleSubmit} className="flex flex-col gap-[5px]">
          <div className="flex flex-col gap-[3px]">
            <label htmlFor="username" className="text-[24px] text-[#878787] font-normal">UserName</label>
            <input
              id="username"
              type="text"
              className="border border-[#D9D9D9] rounded-[10px] px-[6px] py-[4px] text-[24px] focus:outline-none focus:ring-2 focus:ring-[#00B7FF] bg-[#F5F6FA] text-[#222] placeholder:text-[#878787] placeholder:font-normal"
              placeholder="Enter your username"
              value={username}
              onChange={e => setUsername(e.target.value)}
              aria-label="Username"
              required
            />
          </div>
          {error && <div className="text-red-500 text-sm">{error}</div>}
          <div className="flex gap-[5px] mt-[5px]">
            <button
              type="submit"
              className="bg-[#00B7FF] text-white px-[10px] py-[3px] rounded-[10px] font-semibold shadow hover:bg-[#0099cc] transition flex-1 text-[20px]"
            >
              OK
            </button>
            <button
              type="button"
              className="bg-gray-200 text-gray-700 px-[10px] py-[3px] rounded-[10px] font-semibold flex-1 text-[20px]"
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
