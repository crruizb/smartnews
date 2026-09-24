import { useState } from "react";
import { monogram, sourceFavicon } from "../../lib/format";

interface SourceFaviconProps {
  source: string;
  sourceUrl?: string;
  link?: string;
  className?: string;
}

/** Outlet favicon, falling back to a lettered monogram if it can't load. */
export default function SourceFavicon({
  source,
  sourceUrl,
  link,
  className = "h-4 w-4",
}: SourceFaviconProps) {
  const [failed, setFailed] = useState(false);
  const src = sourceFavicon(sourceUrl, link);

  if (!src || failed) {
    return (
      <span
        aria-hidden="true"
        className={`grid shrink-0 place-items-center rounded-[4px] bg-accent-soft text-[9px] font-bold leading-none text-accent-strong ${className}`}
      >
        {monogram(source)}
      </span>
    );
  }

  return (
    <img
      src={src}
      alt=""
      aria-hidden="true"
      loading="lazy"
      onError={() => setFailed(true)}
      className={`shrink-0 rounded-[4px] object-contain ${className}`}
    />
  );
}
