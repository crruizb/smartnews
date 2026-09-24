import { Outlet } from "react-router";
import Footer from "./Footer";

export default function AppLayout() {
  return (
    <div className="flex min-h-screen flex-col bg-paper text-ink">
      <div className="flex flex-1 flex-col">
        <Outlet />
      </div>
      <Footer />
    </div>
  );
}
