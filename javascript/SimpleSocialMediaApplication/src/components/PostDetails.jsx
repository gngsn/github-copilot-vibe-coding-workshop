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
  <div className="min-h-screen bg-[#F5F6FA] flex flex-col items-center justify-start font-sans">
  <div className="w-full max-w-[1280px] mt-[8px] flex flex-col items-center justify-start gap-[8px]">
  <div className="bg-white rounded-[20px] shadow-lg px-[8px] pt-[8px] pb-[6px] mb-[8px] w-full max-w-[760px] mx-auto border border-[#E5A000] flex flex-col gap-[6px]">
          <div className="flex items-center gap-[6px]">
            <img src={`https://ui-avatars.com/api/?name=${post.username}&background=E5A000&color=fff&size=100`} alt={post.username} className="rounded-full w-[100px] h-[100px]" />
            <span className="font-bold text-[#222] text-[32px] leading-tight">{post.username}</span>
            <span className="ml-auto text-xs text-[#878787]">{new Date(post.createdAt).toLocaleString()}</span>
          </div>
          <div className="text-[32px] text-[#222] leading-tight">{post.content}</div>
          <div className="flex gap-[8px] mt-[2px]">
            <span className="flex items-center gap-[2px] text-[#878787] text-[24px]">{post.likes} Likes</span>
            <span className="flex items-center gap-[2px] text-[#878787] text-[24px]">{post.comments} Comments</span>
          </div>
        </div>
  <div className="bg-white rounded-[20px] shadow-lg px-[8px] pt-[8px] pb-[6px] w-full max-w-[760px] mx-auto border border-[#E5A000] flex flex-col gap-[6px]">
          <h3 className="text-[24px] font-bold mb-4 text-[#222]">Comments</h3>
          {comments.length === 0 ? (
            <div className="text-[#878787] text-[20px]">No comments yet.</div>
          ) : (
            <div className="flex flex-col gap-[6px]">
              {comments.map(comment => (
                <div key={comment.id} className="border-b pb-[6px]">
                  <div className="flex items-center gap-[6px]">
                    <img src={`https://ui-avatars.com/api/?name=${comment.username}&background=E5A000&color=fff&size=100`} alt={comment.username} className="rounded-full w-[100px] h-[100px]" />
                    <span className="font-bold text-[#222] text-[24px] leading-tight">{comment.username}</span>
                    <span className="ml-auto text-xs text-[#878787]">{new Date(comment.createdAt).toLocaleString()}</span>
                  </div>
                  <div className="text-[20px] text-[#222] mt-[2px] leading-tight">{comment.content}</div>
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
