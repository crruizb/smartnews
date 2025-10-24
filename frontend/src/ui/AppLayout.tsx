import { Outlet } from "react-router";
import Footer from "./Footer";
import Header from "./Header";

export default function AppLayout() {
    return (
        <div className="min-w-xs flex flex-col min-h-screen">
            <Header />
            <div className="flex flex-1">
                <main className="flex-1 p-4 md:p-8">
                    <Outlet />
                </main>
            </div>
            <Footer />
        </div>
    );
}
