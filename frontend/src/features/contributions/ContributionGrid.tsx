import { motion, useReducedMotion } from "motion/react";
import { ApiContribution } from "../../types";
import Contribution from "./Contribution";
import ContributionSkeleton from "./ContributionSkeleton";

interface ContributionGridProps {
  items: ApiContribution[];
  isLoading?: boolean;
  skeletonCount?: number;
}

const GRID_CLASS = "grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-6 lg:grid-cols-3";

export default function ContributionGrid({
  items,
  isLoading = false,
  skeletonCount = 6,
}: ContributionGridProps) {
  const reduceMotion = useReducedMotion();

  if (isLoading) {
    return (
      <div className={GRID_CLASS}>
        {Array.from({ length: skeletonCount }).map((_, index) => (
          <ContributionSkeleton key={index} />
        ))}
      </div>
    );
  }

  return (
    <div className={GRID_CLASS}>
      {items.map((contribution, index) => (
        <motion.div
          key={contribution.id}
          className="h-full"
          initial={reduceMotion ? false : { opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{
            duration: 0.35,
            ease: "easeOut",
            delay: reduceMotion ? 0 : Math.min(index, 8) * 0.03,
          }}
        >
          <Contribution contribution={contribution} />
        </motion.div>
      ))}
    </div>
  );
}
