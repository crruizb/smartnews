import { SearchX } from "lucide-react";
import { useTranslation } from "react-i18next";
import { useSearchContributions } from "./useContributions";
import { useInfiniteScroll } from "./useInfiniteScroll";
import ContributionGrid from "./ContributionGrid";
import { EmptyState, ErrorState, LoadMoreButton } from "./FeedStates";

interface SearchResultsListProps {
  query: string;
}

export default function SearchResultsList({ query }: SearchResultsListProps) {
  const { t } = useTranslation();
  const { data, fetchNextPage, hasNextPage, isFetching, isPending, error } =
    useSearchContributions(query);

  useInfiniteScroll(hasNextPage, fetchNextPage, isFetching);

  const items = data?.pages.flatMap((page) => page.content) ?? [];

  if (error) {
    return (
      <ErrorState
        title={t("search.errorTitle", "Search failed")}
        description={t(
          "search.error",
          "An error occurred while searching. Please try again.",
        )}
      />
    );
  }

  if (!isPending && items.length === 0) {
    return (
      <EmptyState
        icon={<SearchX className="h-6 w-6" aria-hidden="true" />}
        title={`${t("search.noResults", "No results found for")} “${query}”`}
        description={t(
          "search.noResultsHint",
          "Try a different keyword or check the spelling.",
        )}
      />
    );
  }

  return (
    <div className="flex flex-col">
      <ContributionGrid items={items} isLoading={isPending} skeletonCount={6} />
      {hasNextPage && !isPending && (
        <LoadMoreButton
          onClick={() => fetchNextPage()}
          isFetching={isFetching}
        />
      )}
    </div>
  );
}
