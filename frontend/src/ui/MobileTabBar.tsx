import { useTranslation } from "react-i18next";
import { Newspaper, Sparkles, Star } from "lucide-react";

interface MobileTabBarProps {
  activeSection: string;
  onSectionChange: (section: string) => void;
}

/** App-like bottom navigation, shown only on small screens. */
export default function MobileTabBar({
  activeSection,
  onSectionChange,
}: MobileTabBarProps) {
  const { t } = useTranslation();

  const sections = [
    { id: "latest", label: t("sidebar.latest", "Latest"), Icon: Newspaper },
    { id: "rated", label: t("sidebar.rated", "Rated"), Icon: Star },
    {
      id: "recommendations",
      label: t("sidebar.recommendations", "For you"),
      Icon: Sparkles,
    },
  ];

  return (
    <nav
      aria-label="Sections"
      className="fixed inset-x-0 bottom-0 z-40 border-t border-line bg-paper/95 backdrop-blur-md md:hidden"
      style={{ paddingBottom: "env(safe-area-inset-bottom)" }}
    >
      <ul className="flex items-stretch justify-around">
        {sections.map(({ id, label, Icon }) => {
          const isActive = activeSection === id;
          return (
            <li key={id} className="flex-1">
              <button
                type="button"
                onClick={() => onSectionChange(id)}
                aria-current={isActive ? "page" : undefined}
                className={`flex w-full flex-col items-center gap-1 py-2.5 text-[11px] font-medium transition-colors cursor-pointer ${
                  isActive ? "text-accent-strong" : "text-muted hover:text-ink-2"
                }`}
              >
                <Icon
                  className="h-5 w-5"
                  strokeWidth={isActive ? 2.4 : 2}
                  aria-hidden="true"
                />
                {label}
              </button>
            </li>
          );
        })}
      </ul>
    </nav>
  );
}
