import { useEffect, useState } from "react";
import DarkMode from "./DarkMode";
import Cookies from "js-cookie";
import LanguageSelector from "./LanguageSelector.tsx";
import { useTranslation } from "react-i18next";

interface HeaderProps {
  activeSection?: string;
  onSectionChange?: (section: string) => void;
  showNavigation?: boolean;
}

export default function Header({
  activeSection,
  onSectionChange,
  showNavigation = false,
}: HeaderProps) {
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
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const { t } = useTranslation();

  const sections = [
    { id: "latest", label: t("sidebar.latest", "Latest News"), icon: "📰" },
    { id: "rated", label: t("sidebar.rated", "Rated News"), icon: "⭐" },
    {
      id: "recommendations",
      label: t("sidebar.recommendations", "Recommended"),
      icon: "🎯",
    },
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      const cookieUser = Cookies.get("username");
      setUsername(cookieUser || null);
    }, 10 * 1000);

    return () => clearInterval(interval);
  }, []);

  return (
    <header className="flex flex-col bg-white dark:bg-[#2c2c2c] sticky top-0 z-50 border-b border-pink shadow-md dark:shadow-palid-purple dark:shadow-xs">
      <div className="flex justify-between items-center py-4 px-4 md:px-8">
        <div className="flex items-center gap-4">
          {/* Mobile hamburger button */}
          {showNavigation && (
            <button
              onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
              className="md:hidden p-2 rounded-lg bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors"
              aria-label="Toggle navigation menu"
            >
              <svg
                className="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                {isMobileMenuOpen ? (
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M6 18L18 6M6 6l12 12"
                  />
                ) : (
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M4 6h16M4 12h16M4 18h16"
                  />
                )}
              </svg>
            </button>
          )}
          <div className="flex flex-col items-start">
            <h1 className="text-xl md:text-2xl font-semibold">📰 Smart News</h1>
            {username && (
              <p className="ml-1 text-xs mt-2">Welcome back, {username}</p>
            )}
          </div>
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
      </div>

      {/* Desktop Navigation Tabs */}
      {showNavigation && onSectionChange && (
        <>
          <nav className="hidden md:flex border-t border-stone-200 dark:border-stone-700">
            <div className="max-w-7xl mx-auto px-4 md:px-8 w-full">
              <ul className="flex gap-1">
                {sections.map((section) => (
                  <li key={section.id}>
                    <button
                      onClick={() => onSectionChange(section.id)}
                      className={`px-6 py-3 flex items-center gap-2 transition-colors duration-200 border-b-2 ${
                        activeSection === section.id
                          ? "border-palid-purple text-stone-900 dark:text-white font-medium"
                          : "border-transparent text-gray-600 dark:text-gray-400 hover:text-stone-900 dark:hover:text-white hover:border-gray-300 dark:hover:border-gray-600"
                      }`}
                    >
                      <span className="text-lg">{section.icon}</span>
                      <span className="text-sm">{section.label}</span>
                    </button>
                  </li>
                ))}
              </ul>
            </div>
          </nav>

          {/* Mobile Navigation Menu */}
          {isMobileMenuOpen && (
            <>
              <div
                className="md:hidden fixed inset-0 bg-black bg-opacity-50 z-30"
                onClick={() => setIsMobileMenuOpen(false)}
              />
              <div className="md:hidden fixed top-16 left-0 right-0 bg-white dark:bg-[#2c2c2c] border-b border-stone-200 dark:border-stone-700 z-40 shadow-lg">
                <nav className="p-4">
                  <ul className="space-y-2">
                    {sections.map((section) => (
                      <li key={section.id}>
                        <button
                          onClick={() => {
                            onSectionChange(section.id);
                            setIsMobileMenuOpen(false);
                          }}
                          className={`w-full text-left px-4 py-3 rounded-lg transition-colors duration-200 flex items-center gap-3 ${
                            activeSection === section.id
                              ? "bg-palid-purple text-stone-800 font-medium"
                              : "text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700"
                          }`}
                        >
                          <span className="text-lg">{section.icon}</span>
                          <span>{section.label}</span>
                        </button>
                      </li>
                    ))}
                  </ul>
                </nav>
              </div>
            </>
          )}
        </>
      )}
    </header>
  );
}
