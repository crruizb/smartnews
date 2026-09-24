import { useState } from "react";
import Cookies from "js-cookie";
import { useTranslation } from "react-i18next";
import { Newspaper, Sparkles, Star } from "lucide-react";
import ThemeToggle from "./ThemeToggle";
import LanguageSelector from "./LanguageSelector";
import AccountMenu from "./AccountMenu";
import Logo from "./Logo";

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
  const { t } = useTranslation();
  const [username, setUsername] = useState(() => Cookies.get("username") ?? null);

  const isLocalhost =
    window.location.hostname === "localhost" ||
    window.location.hostname === "127.0.0.1";
  const googleOAuthUrl = isLocalhost
    ? "http://localhost:8080/oauth2/authorization/google"
    : "https://api.cristianruiz.dev/sn/oauth2/authorization/google";

  const sections = [
    { id: "latest", label: t("sidebar.latest", "Latest News"), Icon: Newspaper },
    { id: "rated", label: t("sidebar.rated", "Rated News"), Icon: Star },
    {
      id: "recommendations",
      label: t("sidebar.recommendations", "Recommended"),
      Icon: Sparkles,
    },
  ];

  return (
    <header className="sticky top-0 z-50 border-b border-line bg-paper/85 backdrop-blur-md">
      <div className="container-page flex h-16 items-center justify-between gap-3">
        <div className="flex items-center gap-3 md:gap-6">
          <Logo />
          {showNavigation && onSectionChange && (
            <nav className="hidden items-center gap-1 md:flex" aria-label="Sections">
              {sections.map(({ id, label, Icon }) => {
                const isActive = activeSection === id;
                return (
                  <button
                    key={id}
                    type="button"
                    onClick={() => onSectionChange(id)}
                    aria-current={isActive ? "page" : undefined}
                    className={`flex items-center gap-2 rounded-full px-3.5 py-2 text-sm transition-colors cursor-pointer ${
                      isActive
                        ? "bg-accent-soft font-semibold text-accent-strong"
                        : "font-medium text-ink-2 hover:bg-surface-2 hover:text-ink"
                    }`}
                  >
                    <Icon className="h-4 w-4" aria-hidden="true" />
                    <span>{label}</span>
                  </button>
                );
              })}
            </nav>
          )}
        </div>

        <div className="flex shrink-0 items-center gap-1.5 sm:gap-2">
          <ThemeToggle />
          <LanguageSelector />
          {username ? (
            <AccountMenu
              username={username}
              onSignOut={() => setUsername(null)}
            />
          ) : (
            <a
              href={googleOAuthUrl}
              className="flex shrink-0 items-center gap-2 whitespace-nowrap rounded-full bg-ink px-3.5 py-2 text-xs font-semibold text-paper transition-opacity hover:opacity-90 sm:text-sm"
            >
              <img
                className="h-4 w-4"
                src="https://www.svgrepo.com/show/475656/google-color.svg"
                loading="lazy"
                alt=""
                aria-hidden="true"
              />
              <span className="hidden sm:inline">
                {t("account.signIn", "Sign in with Google")}
              </span>
              <span className="sm:hidden">{t("account.signInShort", "Sign in")}</span>
            </a>
          )}
        </div>
      </div>
    </header>
  );
}
