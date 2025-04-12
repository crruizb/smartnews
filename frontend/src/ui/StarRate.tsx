import { useState } from "react";
import Star from "./Star";
import { useVoteContribution } from "../features/contributions/useContributions";

interface Props {
  rating: number;
  newsId: number;
}

export default function StarRate({ rating, newsId }: Props) {
  const [vote, setVote] = useState(rating);
  const { voteNews } = useVoteContribution();
  const handleSetRating = (vote: number) => {
    voteNews({ id: newsId, rating: vote });
  };

  return (
    <div className="flex items-center">
      <Star
        vote={vote}
        setVote={setVote}
        handleSetRating={handleSetRating}
        order={1}
      />
      <Star
        vote={vote}
        setVote={setVote}
        handleSetRating={handleSetRating}
        order={2}
      />
      <Star
        vote={vote}
        setVote={setVote}
        handleSetRating={handleSetRating}
        order={3}
      />
      <Star
        vote={vote}
        setVote={setVote}
        handleSetRating={handleSetRating}
        order={4}
      />
      <Star
        vote={vote}
        setVote={setVote}
        handleSetRating={handleSetRating}
        order={5}
      />
    </div>
  );
}
