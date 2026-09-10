import { Link, useParams } from "react-router";
import { useTranslation } from "react-i18next";
import { useContribution } from "../features/contributions/useContributions";
import StarRate from "../ui/StarRate";

function ContributionDetail() {
  const { t } = useTranslation();
  const { id } = useParams();
  const contributionId = Number(id);
  const hasValidId = Number.isFinite(contributionId);
  const { data: contribution, error, isPending } = useContribution(contributionId);

  if (hasValidId && isPending) {
    return (
      <div className="max-w-3xl mx-auto px-4 md:px-8 py-8">
        <p className="text-stone-500 dark:text-stone-400">{t("detail.loading")}</p>
      </div>
    );
  }

  if (!hasValidId || error || !contribution) {
    return (
      <div className="max-w-3xl mx-auto px-4 md:px-8 py-8 flex flex-col items-start gap-4">
        <h1 className="text-xl font-semibold">{t("detail.notFound")}</h1>
        <Link
          to="/contributions"
          className="text-sm font-medium text-palid-purple hover:underline"
        >
          {t("detail.back")}
        </Link>
      </div>
    );
  }

  return (
    <article className="max-w-3xl mx-auto px-4 md:px-8 py-6 md:py-8">
      <Link
        to="/contributions"
        className="inline-block text-sm font-medium text-palid-purple hover:underline mb-6"
      >
        ← {t("detail.back")}
      </Link>

      <h1 className="font-semibold text-2xl md:text-3xl leading-tight mb-3">
        {contribution.title}
      </h1>

      <p className="text-xs font-extralight text-stone-500 dark:text-stone-400 mb-6">
        <span className="font-semibold">@{contribution.source}</span>
        {contribution.creator && <> · {contribution.creator}</>} ·{" "}
        {t("contribution.pubDate")}: {contribution.pubDate}
      </p>

      {contribution.urlImage && (
        <div className="overflow-hidden w-full rounded-xl mb-6 bg-stone-200 dark:bg-stone-800">
          <img
            src={contribution.urlImage}
            alt={contribution.title}
            className="w-full h-auto object-cover"
          />
        </div>
      )}

      <p className="font-light text-stone-700 dark:text-stone-200 whitespace-pre-line mb-6">
        {contribution.description}
      </p>

      {contribution.categories.length > 0 &&
        contribution.categories[0] !== "" && (
          <div className="flex gap-1.5 flex-wrap mb-6">
            {contribution.categories.map((c, i) => (
              <span
                key={i}
                className="bg-palid-blue dark:text-black rounded-full px-2.5 py-1 text-xs font-medium capitalize"
              >
                {c}
              </span>
            ))}
          </div>
        )}

      <div className="flex items-center mb-6">
        <StarRate rating={contribution.vote ? contribution.vote : 0} newsId={contribution.id} />
      </div>

      <a
        href={contribution.link}
        target="_blank"
        rel="noopener noreferrer"
        className="inline-block rounded-full bg-palid-pink font-semibold uppercase tracking-wide text-stone-800 transition-colors duration-300 hover:bg-pink px-6 py-3 text-sm"
      >
        {t("detail.readMore", { source: contribution.source })}
      </a>
    </article>
  );
}

export default ContributionDetail;
