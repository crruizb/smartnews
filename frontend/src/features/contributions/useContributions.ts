import { useInfiniteQuery, useMutation } from "@tanstack/react-query";
import {
  getLatestContributions,
  getRatedContributions,
  getRecommendations,
  voteContribution,
} from "../../services/apiContributions";
import toast from "react-hot-toast";
import {useTranslation} from "react-i18next";

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

export function useRatedContributions() {
  const {
    data,
    error,
    fetchNextPage,
    hasNextPage,
  } = useInfiniteQuery({
    queryKey: ["ratedContributions"],
    queryFn: ({ pageParam = 0 }) => getRatedContributions(pageParam),
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

export function useRecommendations() {
  const {
    data,
    error,
    fetchNextPage,
    hasNextPage,
  } = useInfiniteQuery({
    queryKey: ["recommendations"],
    queryFn: ({ pageParam = 0 }) => getRecommendations(pageParam),
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
  const { t } = useTranslation();
  const { mutate: voteNews } = useMutation({
    mutationFn: ({ id, rating }: Vote) => voteContribution(id, rating),
    onSuccess: () => {
      toast.success(t('vote.success'));
    },
    onError: () => {
      toast.error(t('vote.error'));
    },
  });

  return { voteNews };
}
