import { useTranslation } from "react-i18next";
import { Sparkles } from "lucide-react";
import { useRecommendations } from "./useContributions";
import { useInfiniteScroll } from "./useInfiniteScroll";
import ContributionGrid from "./ContributionGrid";
import { EmptyState, LoadMoreButton } from "./FeedStates";

export default function RecommendationsList() {
  const { t } = useTranslation();
  const { data, fetchNextPage, hasNextPage, isPending, error } =
    useRecommendations();

  useInfiniteScroll(hasNextPage, fetchNextPage);

  const items = data?.pages.flatMap((page) => page.content) ?? [];

  if (error || (!isPending && items.length === 0)) {
    return (
      <EmptyState
        icon={<Sparkles className="h-6 w-6" aria-hidden="true" />}
        title={t("recommendations.emptyTitle", "No recommendations yet")}
        description={t(
          "recommendations.empty",
          "No recommendations available yet. Rate some articles to get personalized suggestions!",
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
