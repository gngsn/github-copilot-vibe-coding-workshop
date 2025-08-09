import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const API_BASE = 'http://localhost:8000';

const PostDetails = () => {
  const { postId } = useParams();
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDetails = async () => {
      setLoading(true);
      setError('');
      try {
        const postRes = await fetch(`${API_BASE}/posts/${postId}`);
        if (!postRes.ok) throw new Error('Post not found');
        const postData = await postRes.json();
        setPost(postData);
        const commentsRes = await fetch(`${API_BASE}/posts/${postId}/comments`);
        if (!commentsRes.ok) throw new Error('Comments not found');
        const commentsData = await commentsRes.json();
        setComments(commentsData);
      } catch {
        setError('Failed to load post or comments.');
      } finally {
        setLoading(false);
      }
    };
    fetchDetails();
  }, [postId]);

  if (loading) {
    return <div className="min-h-screen flex items-center justify-center text-gray-500">Loading...</div>;
  }
  if (error) {
    return <div className="min-h-screen flex items-center justify-center text-red-500">{error}</div>;
  }
  if (!post) {
    return <div className="min-h-screen flex items-center justify-center text-gray-400">Post not found.</div>;
  }

  return (
    <div className="min-h-screen bg-[#F5F6FA] flex flex-col items-center">
      <div className="w-full max-w-xl mt-8">
        <div className="bg-white rounded-2xl shadow p-6 mb-6">
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
        <div className="bg-white rounded-2xl shadow p-6">
          <h3 className="text-lg font-bold mb-4 text-[#222]">Comments</h3>
          {comments.length === 0 ? (
            <div className="text-gray-400">No comments yet.</div>
          ) : (
            <div className="flex flex-col gap-4">
              {comments.map(comment => (
                <div key={comment.id} className="border-b pb-4">
                  <div className="flex items-center gap-2">
                    <img src={`https://ui-avatars.com/api/?name=${comment.username}&background=0D8ABC&color=fff&size=24`} alt={comment.username} className="rounded-full w-6 h-6" />
                    <span className="font-semibold text-[#222]">{comment.username}</span>
                    <span className="ml-auto text-xs text-gray-400">{new Date(comment.createdAt).toLocaleString()}</span>
                  </div>
                  <div className="text-sm text-[#222] mt-1">{comment.content}</div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PostDetails;
