import { useEffect, useState } from "react";

interface CommentResponseDTO {
  id: number | string;
  text: string;
  createdAt: string;
}

export default function CommentList() {
  const [comments, setComments] = useState<CommentResponseDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>("");

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await fetch("/comments");
      if (!res.ok) throw new Error("Failed to fetch comments.");
      const data: CommentResponseDTO = await res.json();
      setComments(Array.isArray(data) ? data : []);
    } catch (err: any) {
      setError(err?.message || "Error loading comments.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    const handler = () => load();
    window.addEventListener("pulsedesk:update", handler);
    return () => window.removeEventListener("pulsedesk:update", handler);
  }, []);

  if (loading) return <div className="text-gray-700">Loading comments...</div>;
  if (error) return <div className="text-red-600">{error}</div>;

  return (
    <div className="grid gap-3">
      {comments.map((c) => (
        <div key={c.id} className="border border-gray-300 rounded p-3">
          <p className="text-sm">{c.text}</p>
          <p className="text-xs text-gray-500 mt-1">
            {c.createdAt ? new Date(c.createdAt).toLocaleString() : ""}
          </p>
        </div>
      ))}
      {comments.length === 0 && (
        <div className="text-sm text-gray-600">No comments yet.</div>
      )}
    </div>
  );
}
