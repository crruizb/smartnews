import { useEffect, useState } from "react";
import DarkMode from "./DarkMode";
import Cookies from "js-cookie";
import LanguageSelector from "./LanguageSelector.tsx";

export default function Header() {
  let GOOGLE_OAUTH_URL =
    "https://api.cristianruiz.dev/sn/oauth2/authorization/google";
  const isLocalhost =
    window.location.hostname === "localhost" ||
    window.location.hostname === "127.0.0.1";
  if (isLocalhost) {
    GOOGLE_OAUTH_URL = "http://localhost:8080/oauth2/authorization/google";
  }

  const handleLogin = () => {
    window.location.href = GOOGLE_OAUTH_URL;
  };

  const [username, setUsername] = useState(Cookies.get("username") || null);

  useEffect(() => {
    const interval = setInterval(() => {
      const cookieUser = Cookies.get("username");
      setUsername(cookieUser || null);
    }, 10 * 1000);

    return () => clearInterval(interval);
  }, []);

  return (
    <header className="flex justify-between items-center py-4 px-8 bg-white dark:bg-[#2c2c2c] sticky top-0 z-50 border-b border-pink shadow-md dark:shadow-palid-purple dark:shadow-xs">
      <div className="flex flex-col items-start">
        <h1 className="text-xl md:text-2xl font-semibold">📰 Smart News</h1>
        {username && (
          <p className="ml-1 text-xs mt-2">Welcome back, {username}</p>
        )}
      </div>

      <div className="flex flex-col md:flex-row gap-2 items-end md:items-center">
        {!username && (
          <button
            onClick={handleLogin}
            className="px-4 py-2 border flex items-center gap-2 border-slate-200 dark:border-slate-700 rounded-lg text-slate-700 dark:text-slate-200 hover:border-slate-400 dark:hover:border-slate-500 hover:text-slate-900 dark:hover:text-slate-300 hover:shadow transition duration-150 text-xs md:text-sm cursor-pointer"
          >
            <img
              className="w-5 h-5"
              src="https://www.svgrepo.com/show/475656/google-color.svg"
              loading="lazy"
              alt="google logo"
            />
            <span>Login with Google</span>
          </button>
        )}
        <DarkMode />
        <LanguageSelector />
      </div>
    </header>
  );
}
