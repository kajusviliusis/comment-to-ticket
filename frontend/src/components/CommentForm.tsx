import React, { useState } from "react";

interface SubmitCommentDTO {
  text: string;
}

export default function CommentForm() {
  const [text, setText] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>("");
  const [success, setSuccess] = useState<string>("");

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    if (!text.trim()) {
      setError("Comment cannot be empty.");
      return;
    }
    setLoading(true);
    try {
      const payload: SubmitCommentDTO = { text };
      const res = await fetch("/comments", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error("Failed to submit comment.");
      setText("");
      setSuccess("Comment submitted.");
      window.dispatchEvent(new CustomEvent("pulsedesk:update"));
    } catch (err: any) {
      setError(err?.message || "Error submitting comment.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={submit} className="grid gap-3">
      <label className="block">
        <span className="sr-only">Comment</span>
        <textarea
          className="w-full rounded border border-gray-300 p-2 bg-white focus:outline-none focus:ring-1 focus:ring-gray-300"
          rows={4}
          placeholder="Write your comment..."
          value={text}
          onChange={(e) => setText(e.target.value)}
        />
      </label>
      <div className="flex items-center gap-3 justify-end">
        <button
          type="submit"
          disabled={loading}
          className="px-3 py-2 rounded bg-gray-900 text-white hover:bg-black disabled:opacity-50"
        >
          {loading ? "Submitting..." : "Submit"}
        </button>
        {error && <span className="text-sm text-red-600">{error}</span>}
        {success && <span className="text-sm">{success}</span>}
      </div>
    </form>
  );
}
