import { useState } from "react";
import ContributionsList from "../features/contributions/ContributionsList";
import RatedContributionsList from "../features/contributions/RatedContributionsList";
import RecommendationsList from "../features/contributions/RecommendationsList";
import Sidebar from "../ui/Sidebar";

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
    <div className="flex h-full">
      <Sidebar activeSection={activeSection} onSectionChange={setActiveSection} />
      <div className="flex-1 max-w-4xl mx-auto md:ml-0 px-4 md:px-8">
        {renderContent()}
      </div>
    </div>
  );
}

export default Contributions;
