import './App.css'
import CommentForm from './components/CommentForm';
import CommentList from './components/CommentList';
import TicketList from './components/TicketList';

function App() {
    return (
        <div className="min-h-screen bg-white text-gray-900">
            <header className="border-b border-gray-200">
                <div className="mx-auto max-w-4xl px-4 py-3">
                    <h1 className="text-lg font-semibold">PulseDesk</h1>
                </div>
            </header>

            <main className="mx-auto max-w-4xl px-4 py-4 grid gap-6">
                <section>
                    <h2 className="text-base font-medium mb-2">Submit Comment</h2>
                    <CommentForm />
                </section>

                <section>
                    <h2 className="text-base font-medium mb-2">Comments</h2>
                    <CommentList />
                </section>

                <section>
                    <h2 className="text-base font-medium mb-2">Tickets</h2>
                    <TicketList />
                </section>
            </main>
        </div>
    );
}

export default App;
