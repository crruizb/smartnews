import { useState } from "react";
import { useTranslation } from "react-i18next";
import ContributionsList from "../features/contributions/ContributionsList";
import RatedContributionsList from "../features/contributions/RatedContributionsList";
import RecommendationsList from "../features/contributions/RecommendationsList";
import SearchBar from "../features/contributions/SearchBar";
import SearchResultsList from "../features/contributions/SearchResultsList";
import Header from "../ui/Header";
import MobileTabBar from "../ui/MobileTabBar";

function Contributions() {
  const { t } = useTranslation();
  const [activeSection, setActiveSection] = useState("latest");
  const [searchQuery, setSearchQuery] = useState("");

  const isSearching = searchQuery.trim().length > 0;

  const sectionMeta: Record<string, { title: string; description: string }> = {
    latest: {
      title: t("latestNews.title", "Latest News"),
      description: t(
        "latestNews.description",
        "The most recent stories from your sources.",
      ),
    },
    rated: {
      title: t("ratedNews.title", "Your Rated News"),
      description: t(
        "ratedNews.description",
        "News articles you have previously rated.",
      ),
    },
    recommendations: {
      title: t("recommendations.title", "Recommended for You"),
      description: t(
        "recommendations.description",
        "Personalized picks based on your ratings.",
      ),
    },
  };

  const meta = sectionMeta[activeSection] ?? sectionMeta.latest;

  const handleSectionChange = (section: string) => {
    setActiveSection(section);
    setSearchQuery("");
  };

  const renderContent = () => {
    if (isSearching) {
      return <SearchResultsList query={searchQuery} />;
    }

    switch (activeSection) {
      case "rated":
        return <RatedContributionsList />;
      case "recommendations":
        return <RecommendationsList />;
      default:
        return <ContributionsList />;
    }
  };

  return (
    <div className="flex min-h-full flex-col">
      <Header
        activeSection={activeSection}
        onSectionChange={handleSectionChange}
        showNavigation={true}
      />

      <main className="container-page flex-1 pb-28 pt-6 md:pb-12 md:pt-8">
        <SearchBar key={activeSection} onSearch={setSearchQuery} />

        <section className="mt-8">
          <header className="mb-5">
            <h1 className="font-display text-2xl font-semibold tracking-tight text-ink md:text-3xl">
              {isSearching
                ? `${t("search.resultsFor", "Results for")} “${searchQuery}”`
                : meta.title}
            </h1>
            <p className="mt-1 text-sm text-muted">
              {isSearching
                ? t(
                    "search.resultsHint",
                    "Matching articles across all sources.",
                  )
                : meta.description}
            </p>
          </header>

          {renderContent()}
        </section>
      </main>

      {/* Keep the footer clear of the fixed mobile tab bar. */}
      <div className="h-20 md:hidden" aria-hidden="true" />

      <MobileTabBar
        activeSection={activeSection}
        onSectionChange={handleSectionChange}
      />
    </div>
  );
}

export default Contributions;
