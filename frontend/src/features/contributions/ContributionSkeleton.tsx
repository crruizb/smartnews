/** Placeholder card shown while a page of contributions is loading. */
export default function ContributionSkeleton() {
  return (
    <div className="flex flex-col overflow-hidden rounded-2xl border border-line bg-surface shadow-card">
      <div className="aspect-[16/9] w-full animate-pulse bg-surface-2" />
      <div className="space-y-3 p-4">
        <div className="flex items-center gap-2">
          <div className="h-4 w-4 animate-pulse rounded-[4px] bg-surface-2" />
          <div className="h-3 w-28 animate-pulse rounded-full bg-surface-2" />
        </div>
        <div className="h-4 w-full animate-pulse rounded-full bg-surface-2" />
        <div className="h-4 w-4/5 animate-pulse rounded-full bg-surface-2" />
        <div className="h-3 w-full animate-pulse rounded-full bg-surface-2" />
        <div className="h-3 w-2/3 animate-pulse rounded-full bg-surface-2" />
      </div>
    </div>
  );
}
