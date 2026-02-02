import { useEffect, useState } from "react";

interface TicketResponseDTO {
  id: number | string;
  title: string;
  category: string;
  priority: string;
  shortSummary: string;
  createdAt: string;
}

function priorityClasses(p?: string) {
  const s = (p || "").toLowerCase();
  if (s === "high") return "border-red-300 bg-red-50";
  if (s === "medium") return "border-gray-300 bg-white"; // neutral, no yellow
  return "border-green-300 bg-green-50"; // low or other
}

export default function TicketList() {
  const [tickets, setTickets] = useState<TicketResponseDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>("");

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await fetch("/tickets");
      if (!res.ok) throw new Error("Failed to fetch tickets.");
      const data: TicketResponseDTO = await res.json();
      setTickets(Array.isArray(data) ? data : []);
    } catch (err: any) {
      setError(err?.message || "Error loading tickets.");
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

  if (loading) return <div className="text-gray-700">Loading tickets...</div>;
  if (error) return <div className="text-red-600">{error}</div>;

  return (
    <div className="grid gap-3">
      {tickets.map((t) => (
        <div
          key={t.id}
          className={`border rounded p-3 ${priorityClasses(t.priority)}`}
        >
          <h3 className="font-medium text-center">{t.title}</h3>

          {t.shortSummary && (
            <div className="mt-1 text-sm text-gray-700">{t.shortSummary}</div>
          )}

          <div className="mt-2 text-xs text-gray-700 space-y-1">
            {t.category && (
              <div>
                <span className="text-gray-500">Category:</span> {t.category}
              </div>
            )}
            <div>
              <span className="text-gray-500">Priority:</span> {t.priority || "low"}
            </div>
            <div>
              <span className="text-gray-500">Created:</span> {t.createdAt ? new Date(t.createdAt).toLocaleString() : ""}
            </div>
          </div>
        </div>
      ))}
      {tickets.length === 0 && (
        <div className="text-sm text-gray-600">No tickets found.</div>
      )}
    </div>
  );
}
