import { useInfiniteQuery, useMutation } from "@tanstack/react-query";
import {
  getLatestContributions,
  voteContribution,
} from "../../services/apiContributions";
import toast from "react-hot-toast";

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

interface Vote {
  id: number;
  rating: number;
}

export function useVoteContribution() {
  // const queryClient = useQueryClient();
  const { mutate: voteNews } = useMutation({
    mutationFn: ({ id, rating }: Vote) => voteContribution(id, rating),
    onSuccess: () => {
      toast.success("Voto registrado correctamente!");
    },
    onError: (err) => console.log(err),
  });

  return { voteNews };
}
