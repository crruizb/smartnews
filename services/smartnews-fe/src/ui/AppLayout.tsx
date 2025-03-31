import { Outlet } from "react-router";
import Footer from "./Footer";
import Header from "./Header";

export default function AppLayout() {
  return (
    <div>
      <Header />
      <div className="mx-auto md:p-8 max-w-3xl grid grid-rows-[auto_1fr_auto]">
        <main>
          <Outlet />
        </main>
      </div>
      <Footer />
    </div>
  );
}
