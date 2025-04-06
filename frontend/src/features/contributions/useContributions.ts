import { useInfiniteQuery } from "@tanstack/react-query";
import { getLatestContributions } from "../../services/apiContributions";

export function useContributions(sourceFilter: string) {
  const {
    data,
    error,
    fetchNextPage,
    hasNextPage,
    // isFetching,
    // isFetchingNextPage,
    // status,
  } = useInfiniteQuery({
    queryKey: ["contributions", sourceFilter],
    queryFn: ({ pageParam = 0 }) =>
      getLatestContributions(pageParam, sourceFilter),
    initialPageParam: 0,
    getNextPageParam: (lastPage, _, lastPageParam) => {
      if (lastPage.last) {
        return undefined;
      }
      return lastPageParam + 1;
    },
  });

  return { error, data, fetchNextPage, hasNextPage };
}
