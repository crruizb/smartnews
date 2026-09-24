import { useTranslation } from "react-i18next";
import { Globe } from "lucide-react";

const LANGUAGES = [
  { code: "es", label: "Español" },
  { code: "en", label: "English" },
] as const;

export default function LanguageSelector() {
  const { i18n, t } = useTranslation();
  const current = (i18n.language || "en").slice(0, 2);

  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    i18n.changeLanguage(event.target.value);
    try {
      localStorage.setItem("language", event.target.value);
    } catch {
      /* ignore */
    }
  };

  return (
    <div className="relative flex items-center">
      <Globe
        className="pointer-events-none absolute left-2.5 h-4 w-4 text-muted"
        aria-hidden="true"
      />
      <select
        id="language-select"
        aria-label={t("language.select", "Language")}
        value={current}
        onChange={handleChange}
        className="h-9 cursor-pointer appearance-none rounded-full border border-line bg-surface pl-8 pr-7 text-xs font-medium text-ink-2 transition-colors hover:border-accent hover:text-accent-strong focus:outline-none focus-visible:ring-2 focus-visible:ring-accent"
      >
        {LANGUAGES.map((language) => (
          <option key={language.code} value={language.code}>
            {language.label}
          </option>
        ))}
      </select>
      <svg
        className="pointer-events-none absolute right-2 h-3.5 w-3.5 text-muted"
        viewBox="0 0 20 20"
        fill="none"
        stroke="currentColor"
        strokeWidth={2}
        aria-hidden="true"
      >
        <path d="M6 8l4 4 4-4" strokeLinecap="round" strokeLinejoin="round" />
      </svg>
    </div>
  );
}
