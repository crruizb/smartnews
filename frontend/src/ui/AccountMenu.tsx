import { useEffect, useRef, useState } from "react";
import { LogOut } from "lucide-react";
import Cookies from "js-cookie";
import { useTranslation } from "react-i18next";
import { getLogoutUrl } from "../services/apiContributions";
import { initials } from "../lib/format";

interface AccountMenuProps {
  username: string;
}

export default function AccountMenu({ username }: AccountMenuProps) {
  const { t } = useTranslation();
  const [isOpen, setIsOpen] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!isOpen) return;

    const handlePointerDown = (event: MouseEvent) => {
      if (!containerRef.current?.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") setIsOpen(false);
    };

    document.addEventListener("mousedown", handlePointerDown);
    document.addEventListener("keydown", handleKeyDown);
    return () => {
      document.removeEventListener("mousedown", handlePointerDown);
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [isOpen]);

  const handleSignOut = () => {
    // Clear the readable cookie immediately, then let the backend clear the
    // httpOnly auth cookies and redirect us back to the app.
    Cookies.remove("username");
    window.location.href = getLogoutUrl();
  };

  return (
    <div className="relative" ref={containerRef}>
      <button
        type="button"
        onClick={() => setIsOpen((open) => !open)}
        aria-haspopup="menu"
        aria-expanded={isOpen}
        className="flex shrink-0 items-center gap-2 rounded-full border border-line bg-surface p-0.5 transition-colors hover:border-accent sm:pr-2.5 cursor-pointer"
      >
        <span className="grid h-8 w-8 place-items-center rounded-full bg-accent-soft text-xs font-semibold text-accent-strong">
          {initials(username)}
        </span>
        <span className="hidden max-w-32 truncate text-xs font-medium text-ink-2 sm:block">
          {username}
        </span>
      </button>

      {isOpen && (
        <div
          role="menu"
          className="absolute right-0 z-50 mt-2 w-60 overflow-hidden rounded-2xl border border-line bg-surface shadow-[var(--shadow-lift)]"
        >
          <div className="border-b border-line px-4 py-3">
            <p className="text-[11px] uppercase tracking-wider text-muted">
              {t("account.signedInAs", "Signed in as")}
            </p>
            <p className="mt-0.5 truncate text-sm font-medium text-ink">
              {username}
            </p>
          </div>
          <button
            type="button"
            role="menuitem"
            onClick={handleSignOut}
            className="flex w-full items-center gap-2.5 px-4 py-3 text-left text-sm font-medium text-ink-2 transition-colors hover:bg-accent-soft hover:text-accent-strong cursor-pointer"
          >
            <LogOut className="h-4 w-4" aria-hidden="true" />
            {t("account.signOut", "Sign out")}
          </button>
        </div>
      )}
    </div>
  );
}
