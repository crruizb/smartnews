import { Link, useParams } from "react-router";
import { useTranslation } from "react-i18next";
import { ArrowLeft, Clock, ExternalLink, Share2 } from "lucide-react";
import toast from "react-hot-toast";
import { useContribution } from "../features/contributions/useContributions";
import StarRate from "../ui/StarRate";
import Header from "../ui/Header";
import ReadingProgress from "../ui/ReadingProgress";
import SourceFavicon from "../features/contributions/SourceFavicon";
import { EmptyState } from "../features/contributions/FeedStates";
import { absoluteTime, readingTime } from "../lib/format";

function DetailSkeleton() {
  return (
    <div className="mx-auto max-w-3xl animate-pulse space-y-5 pt-4">
      <div className="h-3 w-24 rounded-full bg-surface-2" />
      <div className="h-9 w-full rounded-full bg-surface-2" />
      <div className="h-9 w-2/3 rounded-full bg-surface-2" />
      <div className="h-4 w-40 rounded-full bg-surface-2" />
      <div className="aspect-[16/9] w-full rounded-2xl bg-surface-2" />
      <div className="space-y-3">
        <div className="h-4 w-full rounded-full bg-surface-2" />
        <div className="h-4 w-full rounded-full bg-surface-2" />
        <div className="h-4 w-3/4 rounded-full bg-surface-2" />
      </div>
    </div>
  );
}

function ContributionDetail() {
  const { t, i18n } = useTranslation();
  const { id } = useParams();
  const contributionId = Number(id);
  const hasValidId = Number.isFinite(contributionId);
  const { data: contribution, error, isPending } = useContribution(contributionId);

  const handleShare = async () => {
    if (!contribution) return;
    const url = window.location.href;

    if (navigator.share) {
      try {
        await navigator.share({ title: contribution.title, url });
        return;
      } catch {
        return; // user dismissed the share sheet
      }
    }

    try {
      await navigator.clipboard.writeText(url);
      toast.success(t("detail.linkCopied", "Link copied to clipboard"));
    } catch {
      toast.error(t("detail.shareError", "Could not copy the link"));
    }
  };

  const isLoading = hasValidId && isPending;

  return (
    <div className="flex min-h-full flex-col">
      <Header />
      <ReadingProgress />

      {isLoading && (
        <div className="container-page flex-1 pt-8 pb-12">
          <DetailSkeleton />
        </div>
      )}

      {!isLoading && (!hasValidId || error || !contribution) && (
        <div className="container-page flex-1 pt-12 pb-12">
          <EmptyState
            title={t("detail.notFound", "We couldn't find that news item.")}
            description={t(
              "detail.notFoundHint",
              "It may have been removed, or the link could be wrong.",
            )}
          />
          <div className="mt-6 text-center">
            <Link
              to="/contributions"
              className="inline-flex items-center gap-2 text-sm font-medium text-accent-strong hover:underline"
            >
              <ArrowLeft className="h-4 w-4" aria-hidden="true" />
              {t("detail.back", "Back to news")}
            </Link>
          </div>
        </div>
      )}

      {!isLoading && contribution && (
        <article className="container-page flex-1 pt-6 pb-14 md:pt-8">
          <div className="mx-auto max-w-3xl">
            <Link
              to="/contributions"
              className="mb-6 inline-flex items-center gap-2 text-sm font-medium text-muted transition-colors hover:text-accent-strong"
            >
              <ArrowLeft className="h-4 w-4" aria-hidden="true" />
              {t("detail.back", "Back to news")}
            </Link>

            <p className="mb-3 text-xs font-semibold uppercase tracking-[0.12em] text-accent-strong">
              {contribution.source}
            </p>

            <h1 className="font-display text-3xl font-semibold leading-[1.15] tracking-tight text-ink md:text-[2.6rem]">
              {contribution.title}
            </h1>

            <div className="mt-5 flex flex-wrap items-center gap-x-3 gap-y-2 text-sm text-muted">
              <span className="flex items-center gap-2">
                <SourceFavicon
                  source={contribution.source}
                  sourceUrl={contribution.sourceUrl}
                  link={contribution.link}
                  className="h-5 w-5"
                />
                <span className="font-medium text-ink-2">
                  {contribution.source}
                </span>
              </span>
              {contribution.creator && (
                <>
                  <span aria-hidden="true">·</span>
                  <span>{contribution.creator}</span>
                </>
              )}
              <span aria-hidden="true">·</span>
              <span>{absoluteTime(contribution.pubDate, i18n.language)}</span>
              <span aria-hidden="true">·</span>
              <span className="flex items-center gap-1">
                <Clock className="h-3.5 w-3.5" aria-hidden="true" />
                {t("detail.readingTime", "{{count}} min read", {
                  count: readingTime(contribution.description),
                })}
              </span>
            </div>

            {contribution.urlImage && (
              <figure className="mt-7 overflow-hidden rounded-2xl border border-line bg-surface-2">
                <img
                  src={contribution.urlImage}
                  alt=""
                  className="h-auto w-full object-cover"
                />
              </figure>
            )}

            <p className="mt-7 whitespace-pre-line text-lg leading-[1.75] text-ink-2">
              {contribution.description}
            </p>

            {contribution.categories.filter(Boolean).length > 0 && (
              <div className="mt-6 flex flex-wrap gap-2">
                {contribution.categories
                  .filter(Boolean)
                  .map((category) => (
                    <span
                      key={category}
                      className="rounded-full bg-accent-soft px-3 py-1 text-xs font-medium capitalize text-accent-strong"
                    >
                      {category}
                    </span>
                  ))}
              </div>
            )}

            <div className="mt-8 flex flex-wrap items-center gap-4 border-t border-line pt-6">
              <StarRate
                rating={contribution.vote ?? 0}
                newsId={contribution.id}
              />

              <div className="ml-auto flex items-center gap-2">
                <button
                  type="button"
                  onClick={handleShare}
                  className="inline-flex items-center gap-2 rounded-full border border-line bg-surface px-4 py-2.5 text-sm font-medium text-ink-2 transition-colors hover:border-accent hover:text-accent-strong cursor-pointer"
                >
                  <Share2 className="h-4 w-4" aria-hidden="true" />
                  {t("detail.share", "Share")}
                </button>

                <a
                  href={contribution.link}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="inline-flex items-center gap-2 rounded-full bg-accent px-5 py-2.5 text-sm font-semibold text-on-accent transition-opacity hover:opacity-90"
                >
                  {t("detail.readMore", "Read full article on {{source}}", {
                    source: contribution.source,
                  })}
                  <ExternalLink className="h-4 w-4" aria-hidden="true" />
                </a>
              </div>
            </div>
          </div>
        </article>
      )}
    </div>
  );
}

export default ContributionDetail;
