import { useEffect } from "react";
import Contribution from "./Contribution";
import { useSearchContributions } from "./useContributions";
import { ApiContribution } from "../../types";
import { useTranslation } from "react-i18next";

interface SearchResultsListProps {
  query: string;
}

export default function SearchResultsList({ query }: SearchResultsListProps) {
  const { t } = useTranslation();
  const { data, fetchNextPage, hasNextPage, error, isFetching } =
    useSearchContributions(query);

  useEffect(() => {
    let ticking = false;

    const handleScroll = () => {
      if (!ticking) {
        window.requestAnimationFrame(() => {
          const scrollTop =
            window.scrollY || document.documentElement.scrollTop;
          if (
            window.innerHeight + scrollTop >=
            document.documentElement.offsetHeight - 100
          ) {
            if (hasNextPage && !isFetching) fetchNextPage();
          }
          ticking = false;
        });
        ticking = true;
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPage, fetchNextPage, isFetching]);

  if (error) {
    return (
      <div className="flex flex-col">
        <hr className="h-px bg-palid-purple dark:bg-pink border-0 my-4" />
        <div className="text-center py-8">
          <p className="text-gray-500 dark:text-gray-400">
            {t("search.error", "An error occurred while searching. Please try again.")}
          </p>
        </div>
      </div>
    );
  }

  const hasResults = data && data.pages.some((page) => page.content.length > 0);

  return (
    <div className="flex flex-col">
      <hr className="h-px bg-palid-purple dark:bg-pink border-0 my-4" />

      {hasResults ? (
        <>
          {data.pages.map((group, groupIndex) => (
            <div
              key={groupIndex}
              className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6 mb-6"
            >
              {group.content.map((c: ApiContribution) => (
                <Contribution contribution={c} key={c.id} />
              ))}
            </div>
          ))}

          {hasNextPage && (
            <button
              onClick={() => fetchNextPage()}
              className="inline-block text-sm rounded-full bg-palid-pink font-semibold uppercase tracking-wide text-stone-800 transition-colors duration-300 hover:bg-pink cursor-pointer w-54 h-10 mx-auto"
            >
              {t("loadMore")}
            </button>
          )}
        </>
      ) : (
        <div className="text-center py-8">
          <p className="text-gray-500 dark:text-gray-400">
            {t("search.noResults", "No results found for")}{" "}
            <span className="font-semibold">"{query}"</span>
          </p>
        </div>
      )}
    </div>
  );
}
