import { useEffect, useState } from "react";
import Contribution from "./Contribution";
import { useContributions } from "./useContributions";
import { ApiContribution } from "../../types";
import { useTranslation } from "react-i18next";

export default function ContributionsList() {
    const { t, i18n } = useTranslation();
    const storedLang = localStorage.getItem("language") || "en";
    const [sourceFilter, setSourceFilter] = useState(storedLang);
    const { data, fetchNextPage, hasNextPage } = useContributions(sourceFilter);

    const SOURCES: Record<string, Record<string, string>> = {
        "es": {
            "es": "Todos",
            "El País": "El País",
            "El Mundo": "El Mundo",
            "20 Minutos": "20 Minutos",
            "ES Diario": "ES Diario",
            "Marca": "Marca",
        },
        "en": {
            "en": "All",
            "NY Times": "NY Times"
        }
    }

  const lang = i18n.language || "en";
  const sourceOptions = SOURCES[lang as keyof typeof SOURCES] || SOURCES["en"];

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

  useEffect(() => {
    setSourceFilter(i18n.language)
  }, [i18n.language]);


  return (
    <div className="flex flex-col">

      <div className="flex gap-2 items-center justify-end mt-4 md:mt-2 mr-2 text-xs md:text-base">
        <div className="px-2">
          <label>{t('source')}:</label>
          <select onChange={(e) => setSourceFilter(e.target.value)}>
              {Object.entries(sourceOptions as Record<string, string>).map(([value, label]) => (
                  <option className="dark:text-black" value={value} key={value}>
                      {label}
                  </option>
              ))}
          </select>
        </div>
      </div>

      <hr className="h-px bg-palid-purple dark:bg-palid-purple border-0 my-4" />
      {data &&
        data.pages.map((group) => (
          <div>
            {group.content.map((c: ApiContribution) => (
              <>
                <Contribution contribution={c} />
                <hr className="h-px bg-palid-purple dark:bg-palid-purple border-0 my-4" />
              </>
            ))}
          </div>
        ))}

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
