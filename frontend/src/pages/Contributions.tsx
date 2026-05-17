import { useState } from "react";
import ContributionsList from "../features/contributions/ContributionsList";
import RatedContributionsList from "../features/contributions/RatedContributionsList";
import RecommendationsList from "../features/contributions/RecommendationsList";
import SearchBar from "../features/contributions/SearchBar";
import SearchResultsList from "../features/contributions/SearchResultsList";
import Header from "../ui/Header";

function Contributions() {
  const [activeSection, setActiveSection] = useState("latest");
  const [searchQuery, setSearchQuery] = useState("");

  const renderContent = () => {
    if (searchQuery.trim().length > 0) {
      return <SearchResultsList query={searchQuery} />;
    }

    switch (activeSection) {
      case "latest":
        return <ContributionsList />;
      case "rated":
        return <RatedContributionsList />;
      case "recommendations":
        return <RecommendationsList />;
      default:
        return <ContributionsList />;
    }
  };

  return (
    <div className="flex flex-col h-full">
      <Header
        activeSection={activeSection}
        onSectionChange={setActiveSection}
        showNavigation={true}
      />
      <div className="flex-1 max-w-7xl mx-auto px-4 md:px-8 w-full">
        <div className="mt-4 md:mt-6">
          <SearchBar onSearch={setSearchQuery} />
        </div>
        {renderContent()}
      </div>
    </div>
  );
}

export default Contributions;
