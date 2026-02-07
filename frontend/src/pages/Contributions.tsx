import { useState } from "react";
import ContributionsList from "../features/contributions/ContributionsList";
import RatedContributionsList from "../features/contributions/RatedContributionsList";
import RecommendationsList from "../features/contributions/RecommendationsList";
import Header from "../ui/Header";

function Contributions() {
  const [activeSection, setActiveSection] = useState("latest");

  const renderContent = () => {
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
        {renderContent()}
      </div>
    </div>
  );
}

export default Contributions;
