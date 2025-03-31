import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import { getLatestContributions } from "../../services/apiContributions";

export function useContributions(sourceFilter: string, dateFilter: string) {
  const {
    data,
    error,
    fetchNextPage,
    hasNextPage,
    isFetching,
    isFetchingNextPage,
    status,
  } = useInfiniteQuery({
    queryKey: ["contributions", sourceFilter, dateFilter],
    queryFn: ({ pageParam = 0 }) =>
      getLatestContributions(pageParam, sourceFilter, dateFilter),
    initialPageParam: 0,
    getNextPageParam: (lastPage, allPages, lastPageParam) => {
      // console.log(lastPage);
      if (lastPage.last) {
        return undefined;
      }
      return lastPageParam + 1;
    },
  });

  return { error, data, fetchNextPage, hasNextPage };
}
