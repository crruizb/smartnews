import { useEffect } from "react";

/**
 * Loads the next page when the user scrolls near the bottom of the document.
 * Uses rAF throttling to keep the scroll handler cheap.
 */
export function useInfiniteScroll(
  hasNextPage: boolean | undefined,
  fetchNextPage: () => void,
  isFetching = false,
) {
  useEffect(() => {
    let ticking = false;

    const handleScroll = () => {
      if (ticking) return;
      ticking = true;

      window.requestAnimationFrame(() => {
        const scrollTop = window.scrollY || document.documentElement.scrollTop;
        const nearBottom =
          window.innerHeight + scrollTop >=
          document.documentElement.offsetHeight - 200;

        if (nearBottom && hasNextPage && !isFetching) {
          fetchNextPage();
        }
        ticking = false;
      });
    };

    window.addEventListener("scroll", handleScroll, { passive: true });
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPage, fetchNextPage, isFetching]);
}
