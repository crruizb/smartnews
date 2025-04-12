import { ApiContribution } from "../../types";
import StarRate from "../../ui/StarRate";

interface Props {
  contribution: ApiContribution;
}

export default function Contribution({ contribution }: Props) {
  return (
    <div className="flex flex-col space-y-2 hover:bg-stone-100 dark:hover:bg-stone-100/5 p-3 rounded-xl transition duration-300">
      <a
        href={contribution.link}
        target="_blank"
        className="flex flex-col space-y-2"
      >
        <p className="text-xs font-extralight">
          Fecha de publicacion: {contribution.pubDate} |{" "}
          <span className="font-semibold">
            @
            <a href={contribution.link} target="_blank">
              {contribution.source}
            </a>
          </span>
        </p>
        <h2 className="font-semibold ">{contribution.title}</h2>
        {contribution.urlImage && (
          <div className="overflow-hidden">
            <img
              src={contribution.urlImage}
              alt={contribution.title}
              className="rounded-xl shadow h-full w-full opacity-80"
            />
          </div>
        )}
        <p className="font-light break-all overflow-hidden text-ellipsis max-w-full">
          {contribution.description.length > 300
            ? `${contribution.description.substring(0, 300)} [...]`
            : contribution.description}
        </p>
      </a>
      {contribution.categories.length > 0 &&
        contribution.categories[0] !== "" && (
          <div className="flex justify-between items-center">
            <StarRate
              rating={contribution.vote ? contribution.vote : 0}
              newsId={contribution.id}
            />

            <div className="flex justify-end text-xs space-x-2">
              {contribution.categories.map((c, i) =>
                i < 3 ? (
                  <p className="bg-palid-blue dark:text-black rounded-xl p-1.5 text-xs font-light capitalize">
                    {c}
                  </p>
                ) : null
              )}
            </div>
          </div>
        )}
    </div>
  );
}
