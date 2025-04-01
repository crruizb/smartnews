import { ApiContribution } from "../../types";

interface Props {
  contribution: ApiContribution;
}

export default function Contribution({ contribution }: Props) {
  return (
    <a
      href={contribution.link}
      target="_blank"
      className="flex flex-col space-y-4 hover:bg-stone-100 p-2 rounded-xl hover:cursor-pointer transition duration-300"
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
      <p className="font-thin">
        {contribution.description.length > 300
          ? `${contribution.description.substring(0, 300)} [...]`
          : contribution.description}
      </p>

      {contribution.categories.length > 0 &&
        contribution.categories[0] !== "" && (
          <div className="flex justify-end text-xs">
            {contribution.categories.map((c, i) =>
              i < 3 ? (
                <p className="bg-green-200 mx-0.5 rounded-xl p-1.5 text-xs font-thin">
                  {c}
                </p>
              ) : null
            )}
          </div>
        )}
    </a>
  );
}
