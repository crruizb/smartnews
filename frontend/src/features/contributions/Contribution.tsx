import { Link } from "react-router";
import { useTranslation } from "react-i18next";
import { ApiContribution } from "../../types";
import StarRate from "../../ui/StarRate";
import SourceFavicon from "./SourceFavicon";
import { absoluteTime, relativeTime } from "../../lib/format";

interface Props {
  contribution: ApiContribution;
}

export default function Contribution({ contribution }: Props) {
  const { i18n } = useTranslation();
  const {
    id,
    title,
    description,
    urlImage,
    source,
    sourceUrl,
    link,
    categories,
    pubDate,
    vote,
  } = contribution;

  const visibleCategories = categories.filter(Boolean).slice(0, 2);

  return (
    <article className="group flex h-full flex-col overflow-hidden rounded-2xl border border-line bg-surface shadow-card transition-all duration-300 hover:-translate-y-0.5 hover:shadow-lift">
      <Link
        to={`/contributions/${id}`}
        className="flex flex-1 flex-col focus-visible:outline-none"
      >
        {urlImage && (
          <div className="aspect-[16/9] w-full overflow-hidden bg-surface-2">
            <img
              src={urlImage}
              alt=""
              loading="lazy"
              className="h-full w-full object-cover transition-transform duration-500 ease-out group-hover:scale-[1.04]"
            />
          </div>
        )}

        <div className="flex flex-1 flex-col gap-2.5 p-4">
          <div className="flex items-center gap-2 text-xs text-muted">
            <SourceFavicon source={source} sourceUrl={sourceUrl} link={link} />
            <span className="truncate font-medium text-ink-2">{source}</span>
            <span aria-hidden="true">·</span>
            <time
              dateTime={pubDate}
              title={absoluteTime(pubDate, i18n.language)}
              className="shrink-0"
            >
              {relativeTime(pubDate, i18n.language)}
            </time>
          </div>

          <h2 className="font-display text-lg font-semibold leading-snug text-ink transition-colors group-hover:text-accent-strong">
            {title}
          </h2>

          {description && (
            <p className="line-clamp-3 text-sm leading-relaxed text-ink-2">
              {description}
            </p>
          )}
        </div>
      </Link>

      <div className="mt-auto flex items-center justify-between gap-2 px-4 pb-4">
        <StarRate rating={vote ?? 0} newsId={id} />
        {visibleCategories.length > 0 && (
          <div className="flex justify-end gap-1.5 overflow-hidden">
            {visibleCategories.map((category) => (
              <span
                key={category}
                className="rounded-full bg-accent-soft px-2.5 py-0.5 text-[11px] font-medium capitalize text-accent-strong"
              >
                {category}
              </span>
            ))}
          </div>
        )}
      </div>
    </article>
  );
}
