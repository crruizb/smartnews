import { useInfiniteQuery, useMutation, useQuery } from "@tanstack/react-query";
import {
  getLatestContributions,
  getRatedContributions,
  getRecommendations,
  searchContributions,
  voteContribution,
  getContribution,
} from "../../services/apiContributions";
import toast from "react-hot-toast";
import { useTranslation } from "react-i18next";

export function useContributions(sourceFilter: string) {
  const { error, data, fetchNextPage, hasNextPage, isPending, isFetching } =
    useInfiniteQuery({
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

  return { error, data, fetchNextPage, hasNextPage, isPending, isFetching };
}

export function useRatedContributions() {
  const { error, data, fetchNextPage, hasNextPage, isPending, isFetching } =
    useInfiniteQuery({
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

  return { error, data, fetchNextPage, hasNextPage, isPending, isFetching };
}

export function useRecommendations() {
  const { error, data, fetchNextPage, hasNextPage, isPending, isFetching } =
    useInfiniteQuery({
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

  return { error, data, fetchNextPage, hasNextPage, isPending, isFetching };
}

export function useSearchContributions(query: string) {
  const { error, data, fetchNextPage, hasNextPage, isFetching, isPending } =
    useInfiniteQuery({
      queryKey: ["searchContributions", query],
      queryFn: ({ pageParam = 0 }) => searchContributions(query, pageParam),
      initialPageParam: 0,
      enabled: query.trim().length > 0,
      getNextPageParam: (lastPage, _, lastPageParam) => {
        if (lastPage.last) {
          return undefined;
        }
        return lastPageParam + 1;
      },
    });

  return { error, data, fetchNextPage, hasNextPage, isFetching, isPending };
}

export function useContribution(id: number) {
  const { data, error, isPending } = useQuery({
    queryKey: ["contribution", id],
    queryFn: () => getContribution(id),
    enabled: Number.isFinite(id),
    retry: false,
  });

  return { data, error, isPending };
}

interface Vote {
  id: number;
  rating: number;
}

export function useVoteContribution() {
  const { t } = useTranslation();
  const { mutate: voteNews } = useMutation({
    mutationFn: ({ id, rating }: Vote) => voteContribution(id, rating),
    onSuccess: () => {
      toast.success(t("vote.success"));
    },
    onError: () => {
      toast.error(t("vote.error"));
    },
  });

  return { voteNews };
}
