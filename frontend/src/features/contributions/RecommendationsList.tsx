import { useEffect } from "react";
import Contribution from "./Contribution";
import { useRecommendations } from "./useContributions";
import { ApiContribution } from "../../types";
import { useTranslation } from "react-i18next";

export default function RecommendationsList() {
  const { t } = useTranslation();
  const { data, fetchNextPage, hasNextPage, error } = useRecommendations();

  useEffect(() => {
    let ticking = false;

    const handleScroll = () => {
      if (!ticking) {
        window.requestAnimationFrame(() => {
          const scrollTop =
            window.scrollY || document.documentElement.scrollTop;
          if (
            window.innerHeight + scrollTop >=
            document.documentElement.offsetHeight
          ) {
            if (hasNextPage) fetchNextPage();
          }
          ticking = false;
        });
        ticking = true;
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPage, fetchNextPage]);

  if (error) {
    return (
      <div className="flex flex-col">
        <div className="mb-4">
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">
            {t('recommendations.title', 'Recommended for You')}
          </h1>
        </div>
        <div className="text-center py-8">
          <p className="text-gray-500 dark:text-gray-400">
            {t('recommendations.error', 'No recommendations available. Please rate some articles first to get personalized recommendations.')}
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex flex-col">

      <hr className="h-px bg-palid-purple dark:bg-pink border-0 my-4" />

      {data && data.pages.length > 0 ? (
        data.pages.map((group, groupIndex) => (
          <div key={groupIndex}>
            {group.content.map((c: ApiContribution) => (
              <div key={c.id}>
                <Contribution contribution={c} />
                <hr className="h-px bg-palid-purple dark:bg-pink border-0 my-4" />
              </div>
            ))}
          </div>
        ))
      ) : (
        <div className="text-center py-8">
          <p className="text-gray-500 dark:text-gray-400">
            {t('recommendations.empty', 'No recommendations available yet. Rate some articles to get personalized suggestions!')}
          </p>
        </div>
      )}

      {hasNextPage && (
        <button
          onClick={() => fetchNextPage()}
          className="inline-block text-sm rounded-full bg-palid-pink font-semibold uppercase tracking-wide text-stone-800 transition-colors duration-300 hover:bg-pink cursor-pointer w-54 h-10 mx-auto"
        >
          {t('loadMore')}
        </button>
      )}
    </div>
  );
}
