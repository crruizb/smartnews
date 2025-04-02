import { useEffect, useState } from "react";
import Contribution from "./Contribution";
import { useContributions } from "./useContributions";
import { ApiContribution } from "../../types";

export default function ContributionsList() {
  const [sourceFilter, setSourceFilter] = useState("all");
  const [dateFilter, setDateFilter] = useState(
    new Date(new Date().setMonth(new Date().getMonth() - 1)).toISOString()
  );
  const { data, fetchNextPage, hasNextPage } = useContributions(
    sourceFilter,
    dateFilter
  );

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
            <option value="all">Todos</option>
            <option value="El País">El País</option>
            <option value="El Mundo">El Mundo</option>
          </select>
        </div>

        <div>
          <label>Fecha:</label>
          <select onChange={(e) => setDateFilter(e.target.value)}>
            <option
              value={new Date(
                new Date().setMonth(new Date().getMonth() - 1)
              ).toISOString()}
            >
              Último mes
            </option>
            <option
              value={new Date(
                new Date().setMonth(new Date().getMonth() - 3)
              ).toISOString()}
            >
              Últimos 3 meses
            </option>
            <option
              value={new Date(
                new Date().setMonth(new Date().getMonth() - 12)
              ).toISOString()}
            >
              Último año
            </option>
            <option
              value={new Date(
                new Date().setFullYear(new Date().getFullYear() - 10)
              ).toISOString()}
            >
              Todos
            </option>
          </select>
        </div>
      </div>

      <hr className="h-px bg-black border-0 my-4" />
      {data &&
        data.pages.map((group) => (
          <div>
            {group.content.map((c: ApiContribution) => (
              <>
                <Contribution contribution={c} />
                <hr className="h-px bg-black border-0 my-4" />
              </>
            ))}
          </div>
        ))}

      {hasNextPage && (
        <button
          onClick={() => fetchNextPage()}
          className="inline-block text-sm rounded-full bg-emerald-400 font-semibold uppercase tracking-wide text-stone-800 transition-colors duration-300 hover:bg-emerald-300 cursor-pointer w-54 h-10 mx-auto"
        >
          Cargar mas noticias
        </button>
      )}
    </div>
  );
}
