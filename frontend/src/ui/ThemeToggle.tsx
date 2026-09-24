import { useEffect, useState } from "react";
import { Moon, Sun } from "lucide-react";
import { useTranslation } from "react-i18next";

/**
 * Light/dark toggle. The initial theme is applied by an inline script in
 * index.html before first paint, so we only need to read it back here.
 */
export default function ThemeToggle() {
  const { t } = useTranslation();
  const [isDark, setIsDark] = useState(() =>
    typeof document !== "undefined"
      ? document.documentElement.classList.contains("dark")
      : false,
  );

  useEffect(() => {
    document.documentElement.classList.toggle("dark", isDark);
    try {
      localStorage.setItem("theme", isDark ? "dark" : "light");
    } catch {
      /* storage may be unavailable (private mode) — ignore */
    }
  }, [isDark]);

  const label = isDark
    ? t("theme.switchToLight", "Switch to light mode")
    : t("theme.switchToDark", "Switch to dark mode");

  return (
    <button
      type="button"
      onClick={() => setIsDark((value) => !value)}
      aria-label={label}
      title={label}
      className="grid h-9 w-9 place-items-center rounded-full border border-line bg-surface text-ink-2 transition-colors hover:border-accent hover:text-accent-strong cursor-pointer"
    >
      {isDark ? (
        <Sun className="h-[18px] w-[18px]" aria-hidden="true" />
      ) : (
        <Moon className="h-[18px] w-[18px]" aria-hidden="true" />
      )}
    </button>
  );
}
