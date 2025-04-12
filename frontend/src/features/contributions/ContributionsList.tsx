import { useEffect, useState } from "react";
import Contribution from "./Contribution";
import { useContributions } from "./useContributions";
import { ApiContribution } from "../../types";

export default function ContributionsList() {
  const [sourceFilter, setSourceFilter] = useState("all");
  const { data, fetchNextPage, hasNextPage } = useContributions(sourceFilter);

  let ticking = false;

  const handleScroll = () => {
    if (!ticking) {
      window.requestAnimationFrame(() => {
        const scrollTop =
          window.pageYOffset || document.documentElement.scrollTop;
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

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPage, fetchNextPage]);

  return (
    <div className="flex flex-col">
      <div className="flex gap-2 items-center justify-end mt-4 md:mt-2 mr-2 text-xs md:text-base">
        <div className="px-2">
          <label>Fuente:</label>
          <select onChange={(e) => setSourceFilter(e.target.value)}>
            <option className="dark:text-black" value="all">
              Todos
            </option>
            <option className="dark:text-black" value="El País">
              El País
            </option>
            <option className="dark:text-black" value="El Mundo">
              El Mundo
            </option>
            <option className="dark:text-black" value="20 Minutos">
              20 Minutos
            </option>
            <option className="dark:text-black" value="ES Diario">
              ES Diario
            </option>
            <option className="dark:text-black" value="Marca">
              Marca
            </option>
          </select>
        </div>
      </div>

      <hr className="h-px bg-palid-purple dark:bg-pink border-0 my-4" />
      {data &&
        data.pages.map((group) => (
          <div>
            {group.content.map((c: ApiContribution) => (
              <>
                <Contribution contribution={c} />
                <hr className="h-px bg-palid-purple dark:bg-pink border-0 my-4" />
              </>
            ))}
          </div>
        ))}

      {hasNextPage && (
        <button
          onClick={() => fetchNextPage()}
          className="inline-block text-sm rounded-full bg-palid-pink font-semibold uppercase tracking-wide text-stone-800 transition-colors duration-300 hover:bg-pink cursor-pointer w-54 h-10 mx-auto"
        >
          Cargar mas noticias
        </button>
      )}
    </div>
  );
}
