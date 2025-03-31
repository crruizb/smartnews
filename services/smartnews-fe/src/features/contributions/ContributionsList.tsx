import { useEffect, useState } from "react";
import Contribution from "./Contribution";
import { useContributions } from "./useContributions";

export default function ContributionsList() {
  const [sourceFilter, setSourceFilter] = useState("all");
  const [dateFilter, setDateFilter] = useState(
    new Date(new Date().setMonth(new Date().getMonth() - 1)).toISOString()
  );
  const { data, fetchNextPage, hasNextPage } = useContributions(
    sourceFilter,
    dateFilter
  );

  const handleScroll = () => {
    if (
      window.innerHeight + document.documentElement.scrollTop !==
      document.documentElement.offsetHeight
    )
      return;
    if (hasNextPage) fetchNextPage();
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPage, fetchNextPage]);

  return (
    <div className="flex flex-col">
      <div className="flex gap-4 items-center justify-end ">
        <div className="px-2">
          <label>Periodico:</label>
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
              Ultimo mes
            </option>
            <option
              value={new Date(
                new Date().setMonth(new Date().getMonth() - 3)
              ).toISOString()}
            >
              Ultimos 3 meses
            </option>
            <option
              value={new Date(
                new Date().setMonth(new Date().getMonth() - 12)
              ).toISOString()}
            >
              Ultimo año
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
        data.pages.map((group, i) => (
          <div>
            {group.content.map((c) => (
              <>
                <Contribution contribution={c} />
                <hr className="h-px bg-black border-0 my-4" />
              </>
            ))}
          </div>
        ))}

      {hasNextPage && (
        <button
          onClick={fetchNextPage}
          className="inline-block text-sm rounded-full bg-yellow-400 font-semibold uppercase tracking-wide text-stone-800 transition-colors duration-300 hover:bg-yellow-300 focus:bg-yellow-300 focus:outline-none focus:ring focus:ring-yellow-300 focus:ring-offset-2 disabled:cursor-not-allowed cursor-pointer w-54 h-10 mx-auto"
        >
          Cargar mas noticias
        </button>
      )}
    </div>
  );
}
