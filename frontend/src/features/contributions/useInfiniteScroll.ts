import { useEffect, useRef } from "react";

/**
 * Loads the next page when the user scrolls near the bottom of the document.
 *
 * Guards against duplicate fetches two ways:
 *  - `isFetching` (from useInfiniteQuery) prevents overlapping requests.
 *  - an in-flight ref prevents firing again in the window between calling
 *    `fetchNextPage` and React Query updating `isFetching`.
 */
export function useInfiniteScroll(
  hasNextPage: boolean | undefined,
  fetchNextPage: () => void,
  isFetching = false,
) {
  const inFlight = useRef(false);

  // Release the guard once the current request has settled.
  useEffect(() => {
    if (!isFetching) inFlight.current = false;
  }, [isFetching]);

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

        if (nearBottom && hasNextPage && !isFetching && !inFlight.current) {
          inFlight.current = true;
          fetchNextPage();
        }
        ticking = false;
      });
    };

    window.addEventListener("scroll", handleScroll, { passive: true });
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPage, fetchNextPage, isFetching]);
}
