import { ApiContribution } from "../../types";
import StarRate from "../../ui/StarRate";
import { useTranslation } from "react-i18next";

interface Props {
  contribution: ApiContribution;
}

export default function Contribution({ contribution }: Props) {
  const { t } = useTranslation();

  return (
    <div className="flex flex-col hover:bg-stone-100 dark:hover:bg-stone-100/5 rounded-xl transition duration-300 overflow-hidden border border-stone-200 dark:border-stone-800">
      <a href={contribution.link} target="_blank" className="flex flex-col">
        {contribution.urlImage && (
          <div className="overflow-hidden w-full h-48 bg-stone-200 dark:bg-stone-800">
            <img
              src={contribution.urlImage}
              alt={contribution.title}
              className="w-full h-full object-cover hover:scale-105 transition-transform duration-300"
            />
          </div>
        )}
        <div className="p-4 space-y-3">
          <div>
            <p className="text-xs font-extralight text-stone-500 dark:text-stone-400 mb-2">
              {t("contribution.pubDate")}: {contribution.pubDate} |{" "}
              <span className="font-semibold">@{contribution.source}</span>
            </p>
            <h2 className="font-semibold text-lg leading-tight">
              {contribution.title}
            </h2>
          </div>
          <p className="font-light text-sm text-stone-600 dark:text-stone-300 line-clamp-3">
            {contribution.description}
          </p>
        </div>
      </a>
      <div className="flex justify-between items-center px-4 pb-4 pt-0">
        <StarRate
          rating={contribution.vote ? contribution.vote : 0}
          newsId={contribution.id}
        />
        {contribution.categories.length > 0 &&
          contribution.categories[0] !== "" && (
            <div className="flex gap-1.5 flex-wrap justify-end">
              {contribution.categories.slice(0, 3).map((c, i) => (
                <span
                  key={i}
                  className="bg-palid-blue dark:text-black rounded-full px-2.5 py-1 text-xs font-medium capitalize"
                >
                  {c}
                </span>
              ))}
            </div>
          )}
      </div>
    </div>
  );
}
