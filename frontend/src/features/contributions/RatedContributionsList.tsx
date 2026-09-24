import { useTranslation } from "react-i18next";
import { Star } from "lucide-react";
import { useRatedContributions } from "./useContributions";
import { useInfiniteScroll } from "./useInfiniteScroll";
import ContributionGrid from "./ContributionGrid";
import { EmptyState, LoadMoreButton } from "./FeedStates";

export default function RatedContributionsList() {
  const { t } = useTranslation();
  const { data, fetchNextPage, hasNextPage, isPending, isFetching, error } =
    useRatedContributions();

  useInfiniteScroll(hasNextPage, fetchNextPage, isFetching);

  const items = data?.pages.flatMap((page) => page.content) ?? [];

  if (error || (!isPending && items.length === 0)) {
    return (
      <EmptyState
        icon={<Star className="h-6 w-6" aria-hidden="true" />}
        title={t("ratedNews.emptyTitle", "Nothing rated yet")}
        description={t(
          "ratedNews.empty",
          "You haven't rated any news yet. Start rating some articles!",
        )}
      />
    );
  }

  return (
    <div className="flex flex-col">
      <ContributionGrid items={items} isLoading={isPending} skeletonCount={3} />
      {hasNextPage && !isPending && (
        <LoadMoreButton onClick={() => fetchNextPage()} />
      )}
    </div>
  );
}
