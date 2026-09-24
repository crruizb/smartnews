import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Newspaper } from "lucide-react";
import { useContributions } from "./useContributions";
import { useInfiniteScroll } from "./useInfiniteScroll";
import ContributionGrid from "./ContributionGrid";
import SourceChips from "./SourceChips";
import { EmptyState, ErrorState, LoadMoreButton } from "./FeedStates";

const SOURCES: Record<string, Record<string, string>> = {
  es: {
    es: "Todos",
    "El País": "El País",
    "El Mundo": "El Mundo",
    "20 Minutos": "20 Minutos",
    "ES Diario": "ES Diario",
    Marca: "Marca",
  },
  en: {
    en: "All",
    "NY Times": "NY Times",
  },
};

export default function ContributionsList() {
  const { t, i18n } = useTranslation();
  const lang = (i18n.language || "en").slice(0, 2);
  const [sourceFilter, setSourceFilter] = useState(lang);

  const { data, fetchNextPage, hasNextPage, isPending, isFetching, error } =
    useContributions(sourceFilter);

  useInfiniteScroll(hasNextPage, fetchNextPage, isFetching);

  useEffect(() => {
    setSourceFilter(lang);
  }, [lang]);

  const sourceOptions = SOURCES[lang] ?? SOURCES.en;
  const options = Object.entries(sourceOptions).map(([value, label]) => ({
    value,
    label,
  }));

  const items = data?.pages.flatMap((page) => page.content) ?? [];
  const isEmpty = !isPending && !error && items.length === 0;

  if (error) {
    return (
      <ErrorState
        title={t("latestNews.errorTitle", "We couldn't load the news")}
        description={t(
          "latestNews.error",
          "Something went wrong while fetching the latest stories. Please try again in a moment.",
        )}
      />
    );
  }

  return (
    <div className="flex flex-col">
      <SourceChips
        options={options}
        value={sourceFilter}
        onChange={setSourceFilter}
      />

      <div className="mt-5">
        <ContributionGrid items={items} isLoading={isPending} skeletonCount={6} />
      </div>

      {isEmpty && (
        <EmptyState
          icon={<Newspaper className="h-6 w-6" aria-hidden="true" />}
          title={t("latestNews.emptyTitle", "No stories yet")}
          description={t(
            "latestNews.empty",
            "There are no articles for this source right now. Try another one.",
          )}
        />
      )}

      {hasNextPage && !isPending && (
        <LoadMoreButton onClick={() => fetchNextPage()} />
      )}
    </div>
  );
}
