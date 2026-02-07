import { Outlet } from "react-router";
import Footer from "./Footer";

export default function AppLayout() {
  return (
    <div className="min-w-xs flex flex-col min-h-screen">
      <div className="flex flex-1">
        <main className="flex-1">
          <Outlet />
        </main>
      </div>
      <Footer />
    </div>
  );
}
