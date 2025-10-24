import { useState } from "react";
import { useTranslation } from "react-i18next";

interface SidebarProps {
  activeSection: string;
  onSectionChange: (section: string) => void;
}

export default function Sidebar({ activeSection, onSectionChange }: SidebarProps) {
  const { t } = useTranslation();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const sections = [
    { id: "latest", label: t("sidebar.latest", "Latest News"), icon: "📰" },
    { id: "rated", label: t("sidebar.rated", "Rated News"), icon: "⭐" },
    { id: "recommendations", label: t("sidebar.recommendations", "Recommended"), icon: "🎯" },
  ];

  const handleSectionChange = (section: string) => {
    onSectionChange(section);
    setIsMobileMenuOpen(false);
  };

  return (
    <>
      {/* Mobile hamburger button */}
      <button
        onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
        className=" md:hidden fixed top-4 left-4 z-50 p-2 rounded-lg bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors"
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

      {/* Mobile backdrop */}
      {isMobileMenuOpen && (
        <div
          className="md:hidden fixed inset-0 bg-white bg-opacity-50 dark:bg-black dark:bg-opacity-50 z-30"
          onClick={() => setIsMobileMenuOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`
          w-52 dark:bg-dark-black md:border-r md:border-palid-purple h-screen overflow-y-auto z-40
          transition-transform duration-300 ease-in-out
          fixed md:sticky md:top-20
          ${isMobileMenuOpen ? 'translate-x-0' : '-translate-x-full md:translate-x-0'}
        `}
      >
        <div className="mr-2 my-4 pr-4">
          <nav>
            <ul className="space-y-2">
              {sections.map((section) => (
                <li key={section.id}>
                  <button
                    onClick={() => handleSectionChange(section.id)}
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
      </aside>
    </>
  );
}
